package fit.edu.se.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import fit.edu.se.dto.user.CustomerResponseDTO;
import fit.edu.se.dto.user.UserResponseDTO;
import fit.edu.se.dto.user.VendorResponseDto;
import reactor.core.publisher.Mono;

@Component
public class UserClient {
	private final WebClient webClient;

	public UserClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://user-service").build();
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

	public Mono<UserResponseDTO> getUserById(String userId) {
	    return webClient.get()
	        .uri("/api/users/{userId}", userId)
	        .retrieve()
	        .bodyToMono(UserResponseDTO.class)
	        .onErrorResume(e -> {
	        	return Mono.error(new RuntimeException("Failed to fetch user: " + e.getMessage()));
	        });
	}
	
	public Mono<CustomerResponseDTO> getCustomerById(String id) {
		return webClient.get()
				.uri("/api/users/customers/{id}", id)
				.retrieve()
				.bodyToMono(CustomerResponseDTO.class)	        
				.onErrorResume(e -> {
		        	return Mono.error(new RuntimeException("Failed to fetch customer: " + e.getMessage()));
		        });
	}
	
	public Mono<VendorResponseDto> getVendorById(String id) {
		return webClient.get()
				.uri("/api/users/vendors/{id}", id)
				.retrieve()
				.bodyToMono(VendorResponseDto.class)	        
				.onErrorResume(e -> {
		        	return Mono.error(new RuntimeException("Failed to fetch vendor: " + e.getMessage()));
		        });
	}
	
}
