package iuh.fit.se.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import iuh.fit.se.dto.product.ProductUpdateAvgRatingRequestDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProductClient {
	private final WebClient webClient;
	
	public ProductClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://product-service").build();
	}
	
	public Mono<Void> callUpdateAvgRating(String reviewId, Double avgRating) {
	    ProductUpdateAvgRatingRequestDto dto = new ProductUpdateAvgRatingRequestDto(avgRating);

	    return webClient.put()
	        .uri("/api/products/{reviewId}/update-avg-rating", reviewId)
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(dto)
	        .retrieve()
	        .toBodilessEntity() // trả về ResponseEntity<Void>
	        .doOnSuccess(response -> {
	            String updatedBy = response.getHeaders().getFirst("X-Updated-Avg-Rating-By");
	            log.info("Được cập nhật bởi: {}", updatedBy);
	        })
	        .then(); // vì bạn chỉ cần Mono<Void>
	}
}
