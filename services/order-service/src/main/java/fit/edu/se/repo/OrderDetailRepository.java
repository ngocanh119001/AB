package fit.edu.se.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import fit.edu.se.model.OrderDetail;
import reactor.core.publisher.Flux;

public interface OrderDetailRepository extends R2dbcRepository<OrderDetail, String> {
	
	Flux<OrderDetail> findByOrderId(String orderId);
}
