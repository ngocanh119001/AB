package fit.edu.se.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import fit.edu.se.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends R2dbcRepository<Order, String> {
//	Mono<Long> countByCreateAtBetween(LocalDateTime start, LocalDateTime end);
	Flux<Order> findByCustomerIdOrderByUpdatedAt(String customerId);
	Flux<Order> findByVendorIdOrderByUpdatedAt(String vendorId);
	Flux<Order> findByCustomerIdAndOrderStateOrderByUpdatedAt(String customerId, String orderState);
	Flux<Order> findByVendorIdAndOrderStateOrderByUpdatedAt(String vendorId, String orderState);
	
	Mono<Long> countByCustomerId(String customerId);
	Mono<Long> countByVendorId(String vendorId);
	Mono<Long> countByCustomerIdAndOrderState(String customerId, String orderState);
	Mono<Long> countByVendorIdAndOrderState(String vendorId, String orderState);
}
