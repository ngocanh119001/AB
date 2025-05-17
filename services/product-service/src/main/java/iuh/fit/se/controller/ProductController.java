package iuh.fit.se.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.product.ProductCreateRequestDto;
import iuh.fit.se.dto.product.ProductIncreaseStockRequest;
import iuh.fit.se.dto.product.ProductResponseDto;
import iuh.fit.se.dto.product.ProductUpdateAvgRatingRequestDto;
import iuh.fit.se.dto.product.ProductUpdateRequestDto;
import iuh.fit.se.dto.product.SearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final iuh.fit.se.service.ProductService productService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<ResponseEntity<ProductResponseDto>> createProduct(
			@Valid @ModelAttribute ProductCreateRequestDto createSanPhamRequestDto) {
		// Logic to create a product
		return productService.createProduct(createSanPhamRequestDto)
				.map(product -> ResponseEntity.status(HttpStatus.CREATED)
                        .header("X-Created-By", "product-service")
                        .body(product));
	}
	
	@GetMapping(value = "/{id}")
	public Mono<ResponseEntity<ProductResponseDto>> getProductById(@PathVariable String id) {
		return productService.getProductById(id)
				.map(product -> ResponseEntity.ok()
						.body(product))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<ResponseEntity<Void>> updateProduct(
			@PathVariable String id,
			@Valid @ModelAttribute ProductUpdateRequestDto updateSanPhamRequestDto) {
		// Logic to update a product
		
		return productService.updateProduct(id, updateSanPhamRequestDto)
				.then(Mono.just(ResponseEntity.status(HttpStatus.OK)
	                    .header("X-Updated-By", "product-service")
	                    .build()));
	}
	@PutMapping(value = "/{id}/increase-stock")
	public Mono<ResponseEntity<Void>> increaseProductQuantity(@PathVariable String id, @RequestBody ProductIncreaseStockRequest req) {
		return productService.increaseProductQuantity(id, req.quantity())
				.then(Mono.just(ResponseEntity.status(HttpStatus.OK)
	                    .header("X-Updated-By", "product-service")
	                    .build()));
	}
	@PutMapping(value = "/{id}/hide")
	public Mono<ResponseEntity<Void>> hideProduct(@PathVariable String id) {
		return productService.toggleHideProduct(id,false)
				.then(Mono.just(ResponseEntity.status(HttpStatus.OK)
	                    .header("X-Hidden-By", "product-service")
	                    .build()));
	}
	
	@PutMapping(value = "/{id}/show")
	public Mono<ResponseEntity<Void>> showProduct(@PathVariable String id) {
		return productService.toggleHideProduct(id,true)
				.then(Mono.just(ResponseEntity.status(HttpStatus.OK)
	                    .header("X-Showed-By", "product-service")
	                    .build()));
	}
	@DeleteMapping(value = "/{id}")
	public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable String id) {
		return productService.deleteProduct(id)
				.then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT)
	                    .header("X-Deleted-By", "product-service")
	                    .build()));
	}
	@PutMapping(value = "/{id}/update-avg-rating")
	public Mono<ResponseEntity<Void>> updateAvgRating(@PathVariable String id, @Valid @RequestBody ProductUpdateAvgRatingRequestDto dto) {
		return productService.updateAvgRating(id, dto.avgRating())
				.then(Mono.just(ResponseEntity.status(HttpStatus.OK)
	                    .header("X-Updated-Avg-Rating-By", "product-service")
	                    .build()));
	}
	
	@GetMapping("/search")
    public Mono<CustomPage<ProductResponseDto>> timKiemSanPham(
            @RequestParam String productName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean isNew ,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String sortKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "60") int size) {
		
		SearchCriteria criteria = new SearchCriteria(productName, minPrice, maxPrice, isNew, minRating, sortKey);

        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(criteria, pageable);
    }
	@GetMapping("/vendors/{vendorId}")
    public Mono<CustomPage<ProductResponseDto>> searchProductByVendorId(
    		@PathVariable String vendorId,
            @RequestParam(defaultValue="") String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByVendorId(vendorId, productName, pageable);
    }
}
