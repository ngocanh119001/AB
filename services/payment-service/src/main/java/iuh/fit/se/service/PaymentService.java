package iuh.fit.se.service;

import iuh.fit.se.dto.BillRequestDto;
import reactor.core.publisher.Mono;

public interface PaymentService {
	Mono<Void> executeNewPayment(Mono<BillRequestDto> dto);
}
