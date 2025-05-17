package fit.iuh.se.seviceImpl;

import org.springframework.stereotype.Service;

import fit.iuh.se.client.ProductClient;
import fit.iuh.se.dto.cartDetail.CartDetailCreateRequestDto;
import fit.iuh.se.dto.cartDetail.CartDetailResponse;
import fit.iuh.se.dto.cartDetail.CartDetailUpdateRequestDto;
import fit.iuh.se.dto.product.ProductResponseDto;
import fit.iuh.se.mapper.CartDetailMapper;
import fit.iuh.se.repo.CartDetailRepository;
import fit.iuh.se.service.CartDetailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults( level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartDetailServiceImpl implements CartDetailService{

	CartDetailMapper cartDetailMapper;
	CartDetailRepository cartDetailRepository;
	ProductClient productClient;
	
	@Override
	public Flux<CartDetailResponse> getAllCartDetails(String userId) {
	    return cartDetailRepository.findByCustomerIdOrderByCreatedAt(userId)
	        
	        .flatMap(cartDetail ->
	        		productClient.getProductById(cartDetail.getProductId())
	        			.zipWith(Mono.just(cartDetailMapper.fromEntityToCartDetailResponse(cartDetail)))
	        			.map(tuple -> {
	        				ProductResponseDto productResponseDto = tuple.getT1(); // Lấy ProductResponse
	                        CartDetailResponse cartDetailResponseDto = tuple.getT2(); // Lấy CartDetailResponse
	                        cartDetailResponseDto.setProduct(productResponseDto); // Cập nhật product
	                        cartDetailResponseDto.setTotalPrice(productResponseDto.getPrice() * cartDetailResponseDto.getQuantity());
	                        return cartDetailResponseDto;
	        			})
	                .onErrorResume(e -> {
	                    log.error("Error fetching product for productId: {}", cartDetail.getProductId(), e);
	                    return Mono.just(cartDetailMapper.fromEntityToCartDetailResponse(cartDetail)); // Trả về cartDetailResponse nếu lỗi
	                })
	        );
	}

	@Override
	public Mono<CartDetailResponse> createCartDetails(CartDetailCreateRequestDto dto) {
		// TODO Auto-generated method stub
		return cartDetailRepository.save(cartDetailMapper.fromCartDetailCreateRequestDtoToEntity(dto))
				.map(cartDetailMapper::fromEntityToCartDetailResponse)
				.flatMap(cartDetailResponse ->
						productClient.getProductById(dto.productId())
							.zipWith(Mono.just(cartDetailResponse))
							.map(tuple -> {
								ProductResponseDto productResponseDto = tuple.getT1(); // Lấy ProductResponse
				                CartDetailResponse cartDetailResponseDto = tuple.getT2(); // Lấy CartDetailResponse
				                cartDetailResponseDto.setProduct(productResponseDto); // Cập nhật product
				                cartDetailResponseDto.setTotalPrice(productResponseDto.getPrice() * cartDetailResponseDto.getQuantity());
				                return cartDetailResponseDto;
							})
						.onErrorResume(e -> {
							log.error("Error fetching product for productId: {}", cartDetailResponse.getProduct().getProductId(), e);
							return Mono.just(cartDetailResponse); // Trả về cartDetailResponse nếu lỗi
						})
				);
	}

	@Override
	public Mono<Void> deleteCartDetail(String cartDetailId) {
		// TODO Auto-generated method stub
		return cartDetailRepository.deleteById(cartDetailId);
	}

	@Override
	public Mono<Void> deleteAllCartDetails(String userId) {
		// TODO Auto-generated method stub
		return cartDetailRepository.deleteByCustomerId(userId);
	}

	@Override
	public Mono<CartDetailResponse> updateCartDetail(String cartDetailId, CartDetailUpdateRequestDto dto) {
		// TODO Auto-generated method stub
		
		 return cartDetailRepository.findById(cartDetailId)
		 		.switchIfEmpty(Mono.error(new RuntimeException("Không tìm thấy CartDetail với ID: " + cartDetailId)))
				.flatMap(cartDetail -> 
					productClient.getProductById(cartDetail.getProductId())
					.flatMap(productResponseDto ->{
						if(dto.quantity()!=null && dto.quantity() > productResponseDto.getStock()) {
							return Mono.error(new RuntimeException("Số lượng sản phẩm không đủ trong kho"));
						}
						return Mono.just(productResponseDto);
						
					})
					
					.zipWith(Mono.just(cartDetail)
							.map(cd -> cartDetailMapper.injectToEntity(cd, dto))
							.flatMap(cartDetailRepository::save)
							.map(cartDetailMapper::fromEntityToCartDetailResponse))
					.map(tuple -> {
						ProductResponseDto productResponseDto = tuple.getT1(); // Lấy ProductResponse
		                CartDetailResponse cartDetailResponseDto = tuple.getT2(); // Lấy CartDetailResponse
		                cartDetailResponseDto.setProduct(productResponseDto); // Cập nhật product
		                cartDetailResponseDto.setTotalPrice(productResponseDto.getPrice() * cartDetailResponseDto.getQuantity());
		                return cartDetailResponseDto;
					}));
	}

}
