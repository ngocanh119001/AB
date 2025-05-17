package fit.edu.se.service;

import fit.edu.se.dto.CustomPage;
import fit.edu.se.dto.order.OrderConcreteResponseDto;
import fit.edu.se.dto.order.OrderRequestDto;
import fit.edu.se.dto.order.OrderResponseDto;
import fit.edu.se.model.OrderState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
//	kết quả trả về là đường dẫn ảnh của vnpay có liên kết tới sepay
	Mono<String> createNewOrderRequest(Mono<OrderRequestDto> orderRequest);
	Mono<Void> insertOrderDetail(Mono<OrderRequestDto> orderRequest);
	Mono<Long> getCountOrder();
	Mono<OrderConcreteResponseDto> getOrderById(String orderId);
	Mono<CustomPage<OrderResponseDto>> getPageOrderByCustomerId(String customerId, int page, int size, OrderState orderState);
	Mono<CustomPage<OrderResponseDto>> getPageOrderByVendorId(String vendorId, int page, int size, OrderState orderState);
	
	Mono<OrderResponseDto> handleOrderStateSuccess(String orderId);
	Mono<OrderResponseDto> handleOrderStateFailed(String orderId);
	
	Mono<Void> deleteOrder(String orderId);
}
