package iuh.fit.se.service;

import org.springframework.data.domain.Pageable;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.product.ProductCreateRequestDto;
import iuh.fit.se.dto.product.ProductResponseDto;
import iuh.fit.se.dto.product.ProductUpdateRequestDto;
import iuh.fit.se.dto.product.SearchCriteria;
import reactor.core.publisher.Mono;

public interface ProductService {
	Mono<ProductResponseDto> createProduct(ProductCreateRequestDto dto);
	Mono<ProductResponseDto> getProductById(String id);
	Mono<CustomPage<ProductResponseDto>> searchProducts(SearchCriteria criteria, Pageable pageable);
	Mono<CustomPage<ProductResponseDto>> getProductsByVendorId(String vendorId, String productName, Pageable pageable);
	
	Mono<Void> updateProduct(String id, ProductUpdateRequestDto dto);
	Mono<Void> increaseProductQuantity(String id, Integer quantity);
	Mono<Void> toggleHideProduct(String id,boolean isHide);
	Mono<ProductResponseDto> updateAvgRating(String id, Double avgRating);
	
	Mono<Void> deleteProduct(String id);
}
