package iuh.fit.se.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.BillRequestDto;
import iuh.fit.se.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class WebHookController {
	/**
	 * This method handles the webhook response from the payment service.
	 * It receives a BillRequestDto object in the request body and logs its content.
	 *
	 * @param dto The BillRequestDto object containing the payment information.
	 * @return A Mono<Void> indicating the completion of the processing.
	 */
	private final PaymentService paymentService;
	
	@PostMapping("/webhook")
	public Mono<Void> billResponse(@RequestBody Mono<BillRequestDto> dto) {
//		lấy thông tin từ content của dto xong rồi tách ra mảng separator là " " để lấy được orderId
//		, sau đó thì từ cache cho vào hàng đợi rabbitMQ
		return  paymentService.executeNewPayment(dto)
				.doOnNext(bill -> log.debug(bill.toString()))
				.doOnError(error -> log.error("Error processing webhook: {}", error.getMessage()));
	}
	
	
	
	
}
