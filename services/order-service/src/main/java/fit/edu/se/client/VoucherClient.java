package fit.edu.se.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import fit.edu.se.dto.voucher.VoucherResponseDto;
import reactor.core.publisher.Mono;

@Component
public class VoucherClient {
	private final WebClient webClient;
	
	public VoucherClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://voucher-service").build();
		
	}
	
	public Mono<VoucherResponseDto> getVoucherById(String voucherId) {
		return webClient.get()
				.uri("/api/vouchers/{voucherId}", voucherId)
				.retrieve()
				.bodyToMono(VoucherResponseDto.class)
				.onErrorResume(e -> {
					System.out.println("Error occurred while fetching voucher: " + e.getMessage());
					return Mono.empty();
				});
	}
	public Mono<VoucherResponseDto> decrementUsesCount(String voucherId) {
		return webClient.put()
				.uri(UriBuilder -> UriBuilder.path("/api/vouchers/{voucherId}/increase-uses-count")
						.queryParam("usesCount", -1)
						.build(voucherId))
				.retrieve()
				.bodyToMono(VoucherResponseDto.class);
	}
	
}
