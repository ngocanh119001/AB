package fit.iuh.se.service;

import fit.iuh.se.dto.cartDetail.CartDetailCreateRequestDto;
import fit.iuh.se.dto.cartDetail.CartDetailResponse;
import fit.iuh.se.dto.cartDetail.CartDetailUpdateRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartDetailService {
	Flux<CartDetailResponse> getAllCartDetails(String userId);
	Mono<CartDetailResponse> createCartDetails(CartDetailCreateRequestDto dto);
	Mono<CartDetailResponse> updateCartDetail(String cartDetailId, CartDetailUpdateRequestDto dto);
	Mono<Void> deleteCartDetail(String cartDetailId);
	Mono<Void> deleteAllCartDetails(String userId);
	
}
