package iuh.fit.se.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Component;

import iuh.fit.se.dto.user.CustomerResponseDTO;
import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.dto.user.VendorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserClient {
	private final WebClient webClient;

	public UserClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://user-service").build();
	}

	public Mono<UserResponseDTO> getUserById(String userId) {
	    return webClient.get()
	        .uri("/api/users/{userId}", userId)
	        .retrieve()
	        .bodyToMono(UserResponseDTO.class)
	        .onErrorResume(e -> {
	        	log.error("Error occurred while fetching user: " + e.getMessage());
	        	log.info("TEST AGAIN");
	        	UserResponseDTO userResponseDTO = UserResponseDTO.builder()
	        			.userId(userId)
	        			.build();
	        	log.info("User not found, returning empty user response: {}", userResponseDTO);
	        	return Mono.just(userResponseDTO);
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
	
		
	
}
