package fit.edu.se.client;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import fit.edu.se.dto.product.ProductResponseDto;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
	private final WebClient webClient;
	
	public ProductClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://product-service").build();
		
	}
	
	private Mono<ClientResponse> jwtFilter(ClientRequest request, ExchangeFunction next) {
        return Mono.deferContextual(ctx -> {
            // Lấy JWT từ context hoặc sử dụng giá trị mặc định
            String jwt = ctx.getOrDefault("jwt", "").toString();
            
            // Tạo request mới nếu có JWT
            ClientRequest filteredRequest = jwt.isEmpty()
                ? request
                : ClientRequest.from(request)
                    .header("Authorization", "Bearer " + jwt)
                    .build();
            
            return next.exchange(filteredRequest);
        });
    }
	
	public Mono<ProductResponseDto> getProductById(String productId) {
		return webClient.get()
				.uri("/api/products/{productId}", productId)
				.retrieve()
				.bodyToMono(ProductResponseDto.class);
	}
	
	public Mono<Void> increaseProductQuantity(String productId, Integer quantity) {
		return webClient.put()
				.uri("/api/products/{productId}/increase-stock", productId)
				.bodyValue(Map.of("quantity", quantity))
				.retrieve()
				.bodyToMono(Void.class);
	}
}
