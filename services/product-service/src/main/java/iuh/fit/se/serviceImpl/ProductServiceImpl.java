package iuh.fit.se.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.filter.BaseProductFilter;
import iuh.fit.se.dto.filter.PriceRangeFilter;
import iuh.fit.se.dto.filter.ProductFilter;
import iuh.fit.se.dto.filter.ProductNewFilter;
import iuh.fit.se.dto.filter.RatingFilter;
import iuh.fit.se.dto.product.ProductCreateRequestDto;
import iuh.fit.se.dto.product.ProductResponseDto;
import iuh.fit.se.dto.product.ProductUpdateRequestDto;
import iuh.fit.se.dto.product.SearchCriteria;
import iuh.fit.se.dto.sort.BestSellingSortStrategy;
import iuh.fit.se.dto.sort.CreatedSortStrategy;
import iuh.fit.se.dto.sort.PriceSortStrategy;
import iuh.fit.se.dto.sort.SortStrategy;
import iuh.fit.se.mapper.ProductMapper;
import iuh.fit.se.model.Images;
import iuh.fit.se.model.Product;
import iuh.fit.se.repo.ProductRepository;
import iuh.fit.se.service.ProductService;
import iuh.fit.se.service.StatisticUpdateService;
import iuh.fit.se.service.ThirdPartyService;
import iuh.fit.se.template.ProductTemplate;
import iuh.fit.se.util.MongoDBQueryUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ThirdPartyService thirdPartyService;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private Map<String, SortStrategy> sortStrategies;
	private final ReactiveMongoTemplate mongoTemplate;
	private final ProductTemplate productTemplate;
	private final StatisticUpdateService statisticUpdateService;
	
	@PostConstruct
    public void initSortStrategies() {
        sortStrategies = new HashMap<>();
        sortStrategies.put("latest", new CreatedSortStrategy(false));
        sortStrategies.put("best_selling", new BestSellingSortStrategy());
        sortStrategies.put("price_asc", new PriceSortStrategy(true));
        sortStrategies.put("price_desc", new PriceSortStrategy(false));
    }

	@Override
	public Mono<ProductResponseDto> createProduct(ProductCreateRequestDto dto) {
		return Mono.just(dto).map(productMapper::toEntity).flatMap(sanPham -> {
			Images images = new Images();
			Mono<String> coverImageMono = dto.getCoverImage() != null
					? thirdPartyService.uploadFile(dto.getCoverImage())
					: Mono.just(null);
			Mono<String> videoMono = dto.getVideo() != null ? thirdPartyService.uploadFile(dto.getVideo())
					: Mono.just(null);
			Flux<String> imageListFlux = Flux.fromIterable(dto.getImages() != null ? dto.getImages() : List.of())
					.flatMap(file -> file != null ? thirdPartyService.uploadFile(file) : Mono.just(null));
			return Mono.zip(coverImageMono, videoMono, imageListFlux.collectList()).map(tuple -> {
				images.setCoverImage(tuple.getT1());
				images.setVideo(tuple.getT2());
				images.setImageList(tuple.getT3());
				sanPham.setImages(images);
				sanPham.setProductId(UUID.randomUUID().toString());
				sanPham.setShow(true);
				sanPham.setSoldCount(0);
				sanPham.setRatingAvg(0.0);
				return sanPham;
			});
		}).flatMap(prod -> productTemplate.save(prod.getProductId(), prod)).map(product -> {
			ProductResponseDto productResponseDto = productMapper.toProductResponseDto(product);
			productResponseDto.setImageList(product.getImages().getImageList());
			productResponseDto.setCoverImage(product.getImages().getCoverImage());
			productResponseDto.setVideo(product.getImages().getVideo());
			productResponseDto.setShow(product.isShow());
			log.info("Product created: {}", productResponseDto);
			return productResponseDto;
		});

	}

	@Override
	public Mono<ProductResponseDto> getProductById(String id) {
		// TODO Auto-generated method stub
		return productTemplate
				.findById(id)
//				.flatMap(product -> statisticUpdateService.updateVisitedProduct(id).thenReturn(product))
				.map(this::mapProductToResponseDto);
	}

	@Override
	public Mono<Void> updateProduct(String id, ProductUpdateRequestDto dto) {
	    log.info("Starting updateProduct for id: {}, dto: {}", id, dto);
	    if (dto == null) {
	        log.error("DTO is null");
	        return Mono.error(new IllegalArgumentException("ProductUpdateRequestDto cannot be null"));
	    }

	    return productRepository.findById(id)
	            .switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)))
	            .doOnNext(product -> {
	            	log.info("Found product: {}", product);
	            	log.info("request dto: {}", dto);
	            	
	            })
	            .flatMap(product -> {
	                // Kiểm tra số lượng ảnh
	                Images currentImages = product.getImages() != null ? product.getImages() : new Images();
	                List<String> currentImageList = currentImages.getImageList() != null 
	                        ? currentImages.getImageList() 
	                        : List.of();

	                log.info("Update product: {}", product);

	                int newImageCount = Optional.ofNullable(dto.getNewImages()).orElse(List.of()).size();
	                int removeImageCount = Optional.ofNullable(dto.getRemoveImages()).orElse(List.of()).size();
	                log.info("Image counts - current: {}, new: {}, remove: {}", 
	                        currentImageList.size(), newImageCount, removeImageCount);

	                if (currentImageList.size() + newImageCount - removeImageCount > 9) {
	                    log.error("Too many images: total would be {}", 
	                            currentImageList.size() + newImageCount - removeImageCount);
	                    return Mono.error(new IllegalArgumentException("Số lượng ảnh tối đa là 9"));
	                }

	                // Upload ảnh mới và xóa ảnh cũ
	                Mono<String> coverImageMono = dto.getCoverImage() != null
	                        ? thirdPartyService.uploadFile(dto.getCoverImage())
	                                .doOnNext(url -> log.info("Uploaded cover image: {}", url))
	                        : Mono.just(currentImages.getCoverImage());
	                log.info("Cover image: {}", dto.getCoverImage() != null ? dto.getCoverImage().filename() : "null");

	                Mono<String> videoMono = dto.getVideo() != null
	                        ? thirdPartyService.uploadFile(dto.getVideo())
	                                .doOnNext(url -> log.info("Uploaded video: {}", url))
	                        : Mono.just(currentImages.getVideo());

	                log.info("New images: {}", dto.getNewImages() != null ? dto.getNewImages():"");
	                
	                Flux<String> uploadNewImagesFlux = Flux
	                        .fromIterable(dto.getNewImages() != null ? dto.getNewImages() : List.of())
	                        .flatMap(file -> file != null ? thirdPartyService.uploadFile(file)
	                                : Mono.just(null)).doOnNext(url -> log.info("Uploaded new image: {}", url)) ;
//	                Flux<String> imageListFlux = Flux.fromIterable(dto.getImages() != null ? dto.getImages() : List.of())
//	    					.flatMap(file -> file != null ? thirdPartyService.uploadFile(file) : Mono.just(null));
	                
	                Flux<Void> deleteOldImagesFlux = Flux
	                        .fromIterable(dto.getRemoveImages() != null ? dto.getRemoveImages() : List.of())
	                        .flatMap(thirdPartyService::deleteFile)
	                        .doOnNext(v -> log.info("Deleted image"))
	                        .onErrorContinue((e, fileName) -> log.warn("Failed to delete file: {}", fileName, e));

	                return Mono.zip(coverImageMono, videoMono, uploadNewImagesFlux.collectList(),
	                        deleteOldImagesFlux.collectList())
	                        .doOnNext(tuple -> log.info("Mono.zip completed: coverImage={}, video={}, newImages={}", 
	                                tuple.getT1(), tuple.getT2(), tuple.getT3()))
	                        .flatMap(tuple -> {
	                            log.info("Entering Mono.zip flatMap");
	                            // Cập nhật thông tin sản phẩm
	                            productMapper.updateFromDto(dto, product);
	                            log.info("Product name: {}", dto.getProductName());
	                            // Cập nhật hình ảnh
	                            Images images = new Images();
	                            images.setCoverImage(
	                                    tuple.getT1() == null ? currentImages.getCoverImage() : tuple.getT1());
	                            images.setVideo(tuple.getT2() == null ? currentImages.getVideo() : tuple.getT2());

	                            // Kết hợp ảnh cũ (đã loại bỏ ảnh bị xóa) và ảnh mới
	                            List<String> updatedImageList = new ArrayList<>(currentImageList);
	                            updatedImageList.removeAll(Optional.ofNullable(dto.getRemoveImages()).orElse(List.of()));
	                            updatedImageList.addAll(tuple.getT3());
	                            images.setImageList(updatedImageList);

	                            product.setImages(images);
	                            return Mono.just(product);
	                        });
	            })
	            .doOnNext(x -> log.info("Product updated: {}", x))
	            .flatMap(product -> {
	                log.info("Saving product: {}", product);
	                return productTemplate.save(product.getProductId(), product);
	            })
	            .doOnSuccess(v -> log.info("Product saved successfully"))
	            .then();
	}

	@Override
	public Mono<Void> deleteProduct(String id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)))
				.flatMap(product -> {
					// Xóa ảnh liên quan đến sản phẩm
					Images images = product.getImages() != null ? product.getImages() : new Images();
					
	                // Collect all image URLs to delete (coverImage, video, and imageList)
	                List<String> urlsToDelete = new ArrayList<>();
	                if (images.getCoverImage() != null) {
	                    urlsToDelete.add(images.getCoverImage());
	                }
	                if (images.getVideo() != null) {
	                    urlsToDelete.add(images.getVideo());
	                }
	                if (images.getImageList() != null) {
	                    urlsToDelete.addAll(images.getImageList());
	                }
	                
	                // Delete all URLs using Flux
	                return Flux.fromIterable(urlsToDelete)
	                        .flatMap(thirdPartyService::deleteFile)
	                        .doOnNext(v -> log.info("Deleted file: {}", v)) // Optional logging
	                        .onErrorContinue((e, url) -> log.warn("Failed to delete file: {}", url, e)) // Handle errors gracefully
	                        .then(productTemplate.delete(id)) // Delete product after all files are deleted
	                        .doOnSuccess(v -> log.info("Product deleted successfully: {}", id));
				});
	}

	public Mono<Void> toggleHideProduct(String id,boolean isHide) {
		return productRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)))
				.flatMap(product -> {
					product.setShow(isHide);
					return productTemplate.save(product.getProductId(), product);
				}).then();
	}

	@Override
	public Mono<ProductResponseDto> updateAvgRating(String id, Double avgRating) {
		// TODO Auto-generated method stub

		return productRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)))
				.flatMap(product -> {
					product.setRatingAvg(avgRating);
					log.info("Product updated with new rating: {}", product);
					return productTemplate.save(product.getProductId(), product);
				}).map(this::mapProductToResponseDto);
	}
	
	public Mono<CustomPage<ProductResponseDto>> searchProducts(SearchCriteria criteria, Pageable pageable) {
	    // Create base query
	    Query query = new Query();
	    query.addCriteria(Criteria.where("productName").regex(criteria.productName(), "i"));
	    query.addCriteria(Criteria.where("isShow").is(true));
	    
	    // Log initial query
	    System.out.println("Initial query: " + query);

	    // Apply filters using Decorator pattern
	    ProductFilter filter = new BaseProductFilter();
	    if (criteria.minPrice() != null && criteria.maxPrice() != null) {
	        filter = new PriceRangeFilter(filter, criteria.minPrice(), criteria.maxPrice());
	    }
	    if (criteria.isNew() != null) {
	        filter = new ProductNewFilter(filter, criteria.isNew());
	    }
	    if (criteria.minRating() != null) {
	        filter = new RatingFilter(filter, criteria.minRating());
	    }

	    query = filter.applyFilter(query);
	    System.out.println("Query after filters: " + query);

	    // Apply sorting using Strategy pattern
	    if (criteria.sort() != null && sortStrategies.containsKey(criteria.sort())) {
	        SortStrategy sortStrategy = sortStrategies.get(criteria.sort());
	        query = sortStrategy.applySort(query, getSortField(criteria.sort()));
	    }
	    System.out.println("Query after sorting: " + query);

	    // Create a new query for counting, copying only the criteria
	    Query countQuery = MongoDBQueryUtil.cloneQuery(query);

	    // Count total elements using the count query
	    Mono<Long> countMono = mongoTemplate.count(countQuery, Product.class);
	    
	    // Apply pagination to the original query for fetching products
	    query.with(pageable);
	    System.out.println("Pageable: page=" + pageable.getPageNumber() + ", size=" + pageable.getPageSize());

	    // Apply pagination and map to response
	    Flux<ProductResponseDto> pagedProducts = mongoTemplate.find(query, Product.class)
	            .map(this::mapProductToResponseDto);

	    // Convert to CustomPage
	    return pagedProducts.collectList().zipWith(countMono).map(tuple -> {
	        List<ProductResponseDto> content = tuple.getT1();
	        long totalElements = tuple.getT2();
	        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

	        System.out.println("totalElements: " + totalElements + 
	                          ", pageSize: " + pageable.getPageSize() + 
	                          ", totalPages: " + totalPages);

	        return new CustomPage<>(
	                content,
	                pageable.getPageNumber(),
	                pageable.getPageSize(),
	                totalElements,
	                totalPages
	        );
	    });
	}
	
	private ProductResponseDto mapProductToResponseDto(Product product) {
		ProductResponseDto productResponseDto = productMapper.toProductResponseDto(product);
		productResponseDto.setImageList(product.getImages().getImageList());
		productResponseDto.setCoverImage(product.getImages().getCoverImage());
		productResponseDto.setVideo(product.getImages().getVideo());
		productResponseDto.setShow(product.isShow());
		return productResponseDto;
	}
	
	private String getSortField(String sortType) {
        switch (sortType) {
            case "latest":
                return "createdAt";
            case "best_selling":
                return "soldCount";
            case "price_asc":
            case "price_desc":
                return "price";
            default:
                return "createdAt";
        }
    }

	@Override
	public Mono<Void> increaseProductQuantity(String id, Integer quantity) {
		// TODO Auto-generated method stub
		
		return productRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + id)))
				.filter(product -> product.getStock() + quantity >= 0)
				.switchIfEmpty(Mono.error(new RuntimeException("Not enough stock: " + id)))
				.flatMap(product -> {
					product.setStock(product.getStock() + quantity);
					if (quantity < 0) {
						product.setSoldCount(product.getSoldCount() - quantity);
					}
					return productTemplate.save(product.getProductId(), product);
				}).then();
	}

	@Override
	public Mono<CustomPage<ProductResponseDto>> getProductsByVendorId(String vendorId, String productName,
			Pageable pageable) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("vendorId").is(vendorId));
		query.addCriteria(Criteria.where("productName").regex(productName, "i"));

		
		Query queryCount = new Query();
		queryCount.addCriteria(Criteria.where("vendorId").is(vendorId));
		queryCount.addCriteria(Criteria.where("productName").regex(productName, "i"));
		// Count total elements
		Mono<Long> countMono = mongoTemplate.count(queryCount, Product.class);
		
		query.with(pageable);		// Fetch products

		// Apply pagination and map to response
		Flux<ProductResponseDto> pagedProducts = mongoTemplate.find(query, Product.class)
				.map(this::mapProductToResponseDto);

		// Convert to CustomPage
		return pagedProducts.collectList().zipWith(countMono).map(tuple -> {
			List<ProductResponseDto> content = tuple.getT1();
			long totalElements = tuple.getT2();
			int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());

			return new CustomPage<>(
					content,
					pageable.getPageNumber(),
					pageable.getPageSize(),
					totalElements,
					totalPages
			);
		});
	}
	
}
