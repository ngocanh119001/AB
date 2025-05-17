package fit.edu.se.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import fit.edu.se.client.ProductClient;
import fit.edu.se.client.UserClient;
import fit.edu.se.client.VoucherClient;
import fit.edu.se.config.RabbitMQConfig;
import fit.edu.se.dto.CustomPage;
import fit.edu.se.dto.order.AddressResponseDto;
import fit.edu.se.dto.order.OrderConcreteResponseDto;
import fit.edu.se.dto.order.OrderDetailResponseDto;
import fit.edu.se.dto.order.OrderRequestDto;
import fit.edu.se.dto.order.OrderResponseDto;
import fit.edu.se.dto.product.ProductResponseDto;
import fit.edu.se.dto.user.VendorResponseDto;
import fit.edu.se.dto.voucher.VoucherEnum;
import fit.edu.se.mapper.AddressMapper;
import fit.edu.se.mapper.OrderDetailMapper;
import fit.edu.se.mapper.OrderMapper;
import fit.edu.se.model.Address;
import fit.edu.se.model.Order;
import fit.edu.se.model.OrderDetail;
import fit.edu.se.model.OrderState;
import fit.edu.se.model.statePattern.PendingState;
import fit.edu.se.model.statePattern.State;
import fit.edu.se.repo.AddressRepository;
import fit.edu.se.repo.OrderDetailRepository;
import fit.edu.se.repo.OrderRepository;
import fit.edu.se.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final AddressMapper addressMapper;
	private final OrderDetailMapper orderDetailMapper;

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final AddressRepository addressRepository;

	private final ProductClient productClient;
	private final RedissonReactiveClient redissonReactiveClient;
	private final VoucherClient voucherClient;
	private final UserClient userClient;
	
	private String ORDER_CREATE_REQUEST_MAP = "order_create_request_map";
	private RMapReactive<String, OrderRequestDto> orderCreateRequestMap;
	private final ReactiveTransactionManager transactionManager;

	private record OrderDetailTemp(ProductResponseDto product, Integer count) {
	}

	@PostConstruct
	public void init() {
		orderCreateRequestMap = redissonReactiveClient.getMap(ORDER_CREATE_REQUEST_MAP,
				new TypedJsonJacksonCodec(String.class, OrderRequestDto.class));
	}

	@Override
	public Mono<String> createNewOrderRequest(Mono<OrderRequestDto> orderRequest) {
		// TODO Auto-generated method stub

		// Chỉ giữ dto nếu orderId không tồn tại
		return orderRequest
				.filterWhen(dto -> orderRepository.findById(dto.getOrderId()).hasElement().map(exists -> !exists)) 
				.flatMap(dto -> Flux.fromIterable(dto.getOrderDetails()).flatMap(od -> {
					return productClient.getProductById(od.productId())
							.switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + od.productId())))
							.flatMap(product -> {
								if (od.quantity() > product.getStock()) {
									return Mono.error(new RuntimeException("Product out of stock: " + od.productId()));
								}
								return Mono.just(new OrderDetailTemp(product, od.quantity()));
							});
				}).collectList()
						.flatMap(products -> {
							if (dto.getVoucherId() != null) {
								return voucherClient.getVoucherById(dto.getVoucherId())
										.switchIfEmpty(Mono.error(
												new RuntimeException("Voucher not found: " + dto.getVoucherId())))
										.flatMap(voucher -> {
											Integer totalPrice = products.stream()
													.mapToInt(p -> (int) Math.round(p.product.getPrice() * p.count))
													.sum();
											if (voucher.getMinPriceRequired() > totalPrice) {
												return Mono.error(new RuntimeException("Invalid voucher discount"));
											}
											if (voucher.getUsesCount() <= 0) {
												return Mono.error(new RuntimeException("Voucher is out of uses"));
											}
											if (voucher.getVoucherType() == VoucherEnum.PERCENT) {
												dto.setTotalPrice((int) Math.round(totalPrice
														- (totalPrice * voucher.getPercentDiscount() / 100)));
											} else if (voucher.getVoucherType() == VoucherEnum.VALUE) {
												dto.setTotalPrice(
														(int) Math.round(totalPrice - voucher.getValueDiscount()));
											}
											log.info("Total price after applying voucher: {}", dto.getTotalPrice());
											return voucherClient.decrementUsesCount(dto.getVoucherId())
													.switchIfEmpty(Mono.error(new RuntimeException("Error decrementing voucher uses count")))
													.thenReturn(dto.getTotalPrice());
										});
							} else {
								return Mono.just(dto.getTotalPrice());
							}
						}).flatMap(totalPrice -> {
							if (dto.getTotalPrice() <= 0) {
								return Mono.error(new RuntimeException("Total price is invalid"));
							}
							log.info("Total price: {}", dto.getTotalPrice());
							return orderCreateRequestMap.fastPut(dto.getOrderId(), dto).thenReturn(dto);
						})
						.flatMap(oDto -> Flux.fromIterable(oDto.getOrderDetails())
									.flatMap(odDto -> productClient.increaseProductQuantity(
											odDto.productId(), 
											-odDto.quantity())
									).collectList()
									.thenReturn(oDto)
						)
						.flatMap(x -> Mono.just(String.format(
								"https://qr.sepay.vn/img?acc=107872989482&bank=VietinBank&des=SEVQR+%s&amount=%d",
								dto.getOrderId(), dto.getTotalPrice()))))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Order request is invalid")));
	}

// nhớ bổ sung thêm phương thức giảm số lượng sản phẩm sau khi thanh toán thành công
	@Override
	public Mono<Void> insertOrderDetail(Mono<OrderRequestDto> orderRequest) {
		// TODO Auto-generated method stub
		TransactionalOperator transactionalOperator = TransactionalOperator.create(transactionManager);
		Mono<Address> addressMono = orderRequest.map(dto -> dto.getDeliveryAddress())
				.map(this.addressMapper::fromAddressRequestDto).map(address -> {
					address.setAddressId(UUID.randomUUID().toString());
					address.setNew(true);
					return address;
				}).doOnNext(address -> log.info("Address: {}", address)).flatMap(this.addressRepository::save);
		Mono<Order> orderMono = orderRequest.map(this.orderMapper::fromOrderRequestDto)
				.zipWith(addressMono, (order, address) -> {
					order.setAddressId(address.getAddressId());
					order.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(5));
					order.setOrderState(OrderState.PENDING);
					order.setStateObject(new PendingState());
					order.setNew(true);
					log.info("Order orderMono: {}", order);
					log.info("Address orderMono: {}", address);
					return order;
				}).flatMap(orderRepository::save);

		Flux<OrderDetail> orderDetailFlux = orderRequest.zipWith(orderMono, (dto, order) -> {
			// Trả về một cặp [dto, order] để sử dụng orderId và orderDetails
			return new Object[] { dto, order };
		}).flatMapMany(pair -> {
			OrderRequestDto dto = (OrderRequestDto) pair[0];
			Order order = (Order) pair[1];
			return Flux.fromIterable(dto.getOrderDetails()).map(orderDetailMapper::fromOrderDetailRequestDto)
					.map(orderDetail -> {
						orderDetail.setOrderId(order.getOrderId()); // Gán orderId từ Order
						return orderDetail;
					})
					.flatMap(orderDetail -> productClient.getProductById(orderDetail.getProductId())
							.switchIfEmpty(Mono
									.error(new RuntimeException("Product not found: " + orderDetail.getProductId())))
							.map(product -> {
								orderDetail.setPrice(product.getPrice() * orderDetail.getQuantity());
								orderDetail.setProductName(product.getProductName());
								orderDetail.setProductImage(product.getCoverImage());
								return orderDetail;
							}))
					.flatMap(orderDetailRepository::save); // Lưu từng OrderDetail
		});
		return transactionalOperator.transactional(orderDetailFlux.then() // Chuyển Flux thành Mono<Void>
		);

	}

	@RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
	public void paymentSuccess(String orderId) {
		// Xử lý đơn hàng ở đây
		insertOrderDetail(orderCreateRequestMap.get(orderId)).then(orderCreateRequestMap.fastRemove(orderId))
				.subscribe();

		log.info("Order with ID {} has been processed successfully.", orderId);

	}

	@Override
	public Mono<Long> getCountOrder() {
		// TODO Auto-generated method stub
		return orderRepository.count().switchIfEmpty(Mono.error(new RuntimeException("Error counting orders")));
	}

	@Override
	public Mono<OrderConcreteResponseDto> getOrderById(String orderId) {
		// TODO Auto-generated method stub
		
		Mono<Order> order = orderRepository.findById(orderId);
		return orderToOrderResponseDto(order)
				.flatMap(dto -> {
					if (dto.getVoucherId() == null) {
						return Mono.just(orderMapper.toOrderConcreteResponseDto(dto));
					}
					return voucherClient
						.getVoucherById(dto.getVoucherId())
						.map(voucher -> {
							OrderConcreteResponseDto concreteDto = orderMapper.toOrderConcreteResponseDto(dto);
							concreteDto.setVoucher(voucher);
							return concreteDto;
						});
				})
				.switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + orderId)));
		
	}

	private Mono<OrderResponseDto> orderToOrderResponseDto(Mono<Order> order) {
		return order.flatMap(o -> {
			Mono<AddressResponseDto> addressMono = addressRepository.findById(o.getAddressId())
	                .map(addressMapper::toAddressResponseDto)
	                .switchIfEmpty(Mono.error(new RuntimeException("Không tìm thấy thông tin địa chỉ")));
			Mono<List<OrderDetailResponseDto>> orderDetailsMono = 
					orderDetailRepository.findByOrderId(o.getOrderId())
					.map(orderDetailMapper::toOrderDetailResponseDto)
					.collectList();
			Mono<VendorResponseDto> vendorDto = userClient.getVendorById(o.getVendorId());
			return Mono.zip(addressMono, orderDetailsMono,vendorDto)
					.map(tuple -> {
						AddressResponseDto address = tuple.getT1();
						List<OrderDetailResponseDto> orderDetails = tuple.getT2();
						OrderResponseDto dto = orderMapper.toOrderResponseDto(o);
						dto.setDeliveryAddress(address);
						dto.setOrderDetails(orderDetails);
						dto.setVendor(tuple.getT3());
						return dto;
					});
		});
	}
	
 	private Mono<CustomPage<OrderResponseDto>> fluxToCustomPage(
			Flux<OrderResponseDto> orderFlux, 
			Mono<Long> totalElementsMono,
			int page, 
			int size
	) {
		return orderFlux.collectList()
				.zipWith(totalElementsMono)
				.map(tuple -> {
					List<OrderResponseDto> dto = tuple.getT1();
					Long totalElements = tuple.getT2();
					CustomPage<OrderResponseDto> customPage = new CustomPage<>();
					customPage.setContent(dto);
					customPage.setPage(page);
					customPage.setSize(size);
					customPage.setTotalElements(totalElements);
					customPage.setTotalPages((int) Math.ceil((double) totalElements / size));
					return customPage;
				});
	}
	
	@Override
	public Mono<CustomPage<OrderResponseDto>> getPageOrderByCustomerId(
			String customerId, int page, int size, OrderState orderState) {
		// TODO Auto-generated method stub
		
		Flux<Order> orderFlux;
		Mono<Long> totalElementsMono;
		
		if (orderState == null) {
			orderFlux = orderRepository.findByCustomerIdOrderByUpdatedAt(customerId)
		            .skip(page * size)
		            .take(size);
			totalElementsMono = orderRepository.countByCustomerId(customerId);
		}
		else {
			orderFlux = orderRepository.findByCustomerIdAndOrderStateOrderByUpdatedAt(customerId, orderState.toString())
		            .skip(page * size)
		            .take(size);
			totalElementsMono = orderRepository.countByCustomerIdAndOrderState(customerId, orderState.toString());
		}
		
		
		
		Flux<OrderResponseDto> orderDtoFlux = orderFlux
				.flatMap(order -> orderToOrderResponseDto(Mono.just(order))
		);
		return fluxToCustomPage(orderDtoFlux,totalElementsMono, page, size);
//		.switchIfEmpty(Mono.error(new RuntimeException("Order not found for customerId: " + customerId)));
	}

	@Override
	public Mono<CustomPage<OrderResponseDto>> getPageOrderByVendorId(
			String vendorId, 
			int page, 
			int size,
			OrderState orderState
	) {
		Flux<Order> orderFlux;
		Mono<Long> totalElementsMono;
		
		if (orderState == null) {
			orderFlux = orderRepository.findByVendorIdOrderByUpdatedAt(vendorId)
		            .skip(page * size)
		            .take(size);
			totalElementsMono = orderRepository.countByVendorId(vendorId);
		}
		else {
			orderFlux = orderRepository.findByVendorIdAndOrderStateOrderByUpdatedAt(vendorId, orderState.toString())
		            .skip(page * size)
		            .take(size);
			totalElementsMono = orderRepository.countByVendorIdAndOrderState(vendorId, orderState.toString());
		}
		
		Flux<OrderResponseDto> orderDtoFlux = orderFlux
				.flatMap(order -> orderToOrderResponseDto(Mono.just(order))
		);
		return fluxToCustomPage(orderDtoFlux,totalElementsMono, page, size);
	}

	@Override
	public Mono<OrderResponseDto> handleOrderStateSuccess(String orderId) {
		Mono<Order> monoOrder = orderRepository.findById(orderId)
				.filter(order -> order.getStateObject() != null)
				.switchIfEmpty(Mono.error(new RuntimeException("Order State something wrong: " + orderId)))
				.map(order -> {
					State stateObject = order.getStateObject();
					stateObject.handleSuccess(order);
					return order;
				})
				.flatMap(orderRepository::save);
		return orderToOrderResponseDto(monoOrder)
				.switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + orderId)));
	}

	@Override
	public Mono<OrderResponseDto> handleOrderStateFailed(String orderId) {
		// TODO Auto-generated method stub
		Mono<Order> monoOrder = orderRepository.findById(orderId)
				.filter(order -> order.getStateObject() != null)
				.switchIfEmpty(Mono.error(new RuntimeException("Order State is null: " + orderId)))
				.filter(order -> order.getOrderState() != OrderState.PENDING)
				.switchIfEmpty(Mono.error(new RuntimeException("Order State pending must delete not handle fail: " + orderId)))
				.filter(order -> order.getOrderState() != OrderState.SUCCEEDED)
				.switchIfEmpty(Mono.error(new RuntimeException("Order State Succeeded can't handle fail: " + orderId)))
				.map(order -> {
					State stateObject = order.getStateObject();
					stateObject.handleFailure(order);
					return order;
				})
				.flatMap(orderRepository::save)
				.doOnNext(order -> {
					log.info("Order infomation: {}",order);
				})
				.flatMap(order -> orderDetailRepository.findByOrderId(order.getOrderId())
										.doOnNext(orderDetail -> {
											log.info("OrderDetail infomation: {}",orderDetail);
										})
										.flatMap(orderDetail-> 
											productClient.increaseProductQuantity(orderDetail.getProductId(), orderDetail.getQuantity()))
										.collectList()
										.then(Mono.just(order)) // Chuyển đổi thành Mono<Order>
						);
		return orderToOrderResponseDto(monoOrder)
				.switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + orderId)));
	}

	@Override
	public Mono<Void> deleteOrder(String orderId) {
		TransactionalOperator transactionalOperator = TransactionalOperator.create(transactionManager);
	    return orderRepository.findById(orderId)
	            .switchIfEmpty(Mono.error(new RuntimeException("Order not found: " + orderId)))
	            .flatMap(order -> {
	                if (order.getOrderState() != OrderState.PENDING) {
	                	return Mono.error(new RuntimeException("Cannot delete order with state: " + order.getOrderState()));
	                }
	                
	                return transactionalOperator.transactional(
	                    orderDetailRepository.findByOrderId(orderId)
	                        .flatMap(orderDetail -> 
	                            productClient.increaseProductQuantity(
	                                orderDetail.getProductId(), 
	                                orderDetail.getQuantity()
	                            )
	                            .then(orderDetailRepository.deleteById(orderDetail.getOrderDetailId()))
	                        )
	                        .then()
	                        .then(orderRepository.deleteById(orderId))
	                        .then(addressRepository.deleteById(order.getAddressId()))
	                );
	            });
	}
	
	
}
