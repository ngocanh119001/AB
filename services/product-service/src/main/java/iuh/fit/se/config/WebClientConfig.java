package iuh.fit.se.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	@Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(jwtExchangeFilterFunction());
    }

    private ExchangeFilterFunction jwtExchangeFilterFunction() {
        return (clientRequest, next) -> Mono.deferContextual(contextView -> {
            // Lấy JWT từ Context
            String jwt = contextView.getOrDefault("jwt", "");
            
            // Nếu có JWT, thêm vào header
            if (!jwt.isEmpty()) {
                ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .header("Authorization", "Bearer " + jwt)
                    .build();
                return next.exchange(newRequest);
            }
            
            return next.exchange(clientRequest);
        });
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}