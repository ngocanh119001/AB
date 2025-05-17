package iuh.fit.se.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ReviewClient {
	private final WebClient webClient;
	
	public ReviewClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://review-service").build();
		
	}
}
