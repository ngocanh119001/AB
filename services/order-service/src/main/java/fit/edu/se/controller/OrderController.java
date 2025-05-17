package fit.edu.se.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fit.edu.se.dto.CustomPage;
import fit.edu.se.dto.order.OrderConcreteResponseDto;
import fit.edu.se.dto.order.OrderRequestDto;
import fit.edu.se.dto.order.OrderResponseDto;
import fit.edu.se.model.OrderState;
import fit.edu.se.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	private final OrderService orderService;
	
	@GetMapping(path = "count")
	public Mono<Long> getCountOrder() {
		return orderService.getCountOrder();
	}
	
	@GetMapping(path = "/{id}")
	public Mono<OrderConcreteResponseDto> getOrder(@PathVariable String id) {
		return orderService.getOrderById(id);
	}
	
	@GetMapping(path = "vendor/{vendorId}")
	public Mono<ResponseEntity<CustomPage<OrderResponseDto>>> getOrdersByVendorId(
			@PathVariable String vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) OrderState orderState
//            @RequestHeader("Authorization") String authHeader
		) {
//		String jwt = authHeader.replace("Bearer ", "");
		
		return orderService.getPageOrderByVendorId(vendorId, page, size, orderState)
				.map(customPage -> ResponseEntity.ok(customPage))
//				.contextWrite(Context.of("jwt", jwt))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	
	
	@GetMapping(path = "customer/{customerId}")
	public Mono<ResponseEntity<CustomPage<OrderResponseDto>>> getOrdersByCustomerId(
			@PathVariable String customerId,
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) OrderState orderState
	) {
		return orderService.getPageOrderByCustomerId(customerId, page, size, orderState)
				.map(customPage -> ResponseEntity.ok(customPage))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<String> createOrderRequest(@RequestBody Mono<OrderRequestDto> orderRequestDto) {
		return orderService.createNewOrderRequest(orderRequestDto)
				.doOnNext(x -> log.info("TestAPI"));
	}
	
	@PutMapping(path = "/{orderId}/handleSuccess")
	public Mono<OrderResponseDto> handleSuccess(@PathVariable String orderId) {
		return orderService.handleOrderStateSuccess(orderId);
	}
	
	@PutMapping(path = "/{orderId}/handleFail")
	public Mono<OrderResponseDto> handleFail(@PathVariable String orderId) {
		return orderService.handleOrderStateFailed(orderId);
	}
	
	@DeleteMapping(path = "/{orderId}")
	public Mono<Void> deleteOrder(@PathVariable String orderId) {
		return orderService.deleteOrder(orderId);
	}
	
	
}
