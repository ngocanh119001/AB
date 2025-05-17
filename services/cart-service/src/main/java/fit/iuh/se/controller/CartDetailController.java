package fit.iuh.se.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fit.iuh.se.dto.cartDetail.CartDetailCreateRequestDto;
import fit.iuh.se.dto.cartDetail.CartDetailResponse;
import fit.iuh.se.dto.cartDetail.CartDetailUpdateRequestDto;
import fit.iuh.se.service.CartDetailService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cart-detail")
@RequiredArgsConstructor
public class CartDetailController {
	
	private final CartDetailService cartDetailService;
	
	@GetMapping(path = "/customer/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CartDetailResponse> getAllCartDetails(@PathVariable String userId) {
		return cartDetailService.getAllCartDetails(userId);
		 
	}
	
	@PostMapping
	public Mono<CartDetailResponse> createCartDetails(@RequestBody CartDetailCreateRequestDto dto) {
		return cartDetailService.createCartDetails(dto);
	}
	@PutMapping(path ="/{cartDetailId}")
	public Mono<CartDetailResponse> updateCartDetail(@PathVariable String cartDetailId, @RequestBody CartDetailUpdateRequestDto dto) {
		return cartDetailService.updateCartDetail(cartDetailId, dto);
	}
	@DeleteMapping(path ="/{cartDetailId}")
	public Mono<Void> deleteCartDetail(@PathVariable String cartDetailId) {
		return cartDetailService.deleteCartDetail(cartDetailId);
	}
	@DeleteMapping(path ="/customer/{userId}")
	public Mono<Void> deleteAllCartDetailsFromCustomer(@PathVariable String userId) {
		return cartDetailService.deleteAllCartDetails(userId);
	}
}
