package iuh.fit.se.serviceImpl;

import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import iuh.fit.se.config.RabbitMQConfig;
import iuh.fit.se.dto.BillRequestDto;
import iuh.fit.se.dto.order.OrderRequestDto;
import iuh.fit.se.service.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{

	private final RabbitTemplate rabbitTemplate;
	private final RedissonReactiveClient redissonReactiveClient;
	private String ORDER_CREATE_REQUEST_MAP = "order_create_request_map";
	private RMapReactive<String, OrderRequestDto> orderCreateRequestMap;
	

	@PostConstruct
	public void init() {
		// TODO Auto-generated method stub
		orderCreateRequestMap = redissonReactiveClient.getMap(ORDER_CREATE_REQUEST_MAP, 
				new TypedJsonJacksonCodec(String.class, OrderRequestDto.class));
	}
	
	
	private Mono<Void> addOrderIdIntoCache(String orderId) {
		// TODO Auto-generated method stub
		return Mono.fromRunnable(() -> {
            // Gửi order vào RabbitMQ queue
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                orderId
            );
        });
	}


	@Override
	public Mono<Void> executeNewPayment(Mono<BillRequestDto> dto) {
	    return dto.map(bill -> {
	        String[] contents = bill.content().split(" ");
	        String orderId = contents[contents.length - 1];
	        Integer amount = bill.transferAmount();
	        log.info("OrderId: " + orderId);
	        return new PayMentObj(orderId, amount);
	    })
	    .flatMap(paymentObj -> {
	        log.info("PaymentObj: " + paymentObj);
	        return orderCreateRequestMap.get(paymentObj.orderId())
	                .filter(cacheDto -> {
	                    log.info("CacheDto: " + cacheDto);
	                    return cacheDto.getTotalPrice().equals(paymentObj.amount());
	                })
	                .switchIfEmpty(Mono.error(new RuntimeException("Payment amount does not match order total: " + paymentObj.orderId())))
	                .thenReturn(paymentObj);
	    })
	    .flatMap(paymentObj -> {
	        return addOrderIdIntoCache(paymentObj.orderId())
	                .doOnSuccess(x -> log.info("Add orderId into cache: {}",paymentObj.orderId()));
	    })
	    .doOnError(error -> log.error("Error processing webhook: {}", error.getMessage()));
	}

	private record PayMentObj(String orderId, Integer amount) {
		
	}
}
