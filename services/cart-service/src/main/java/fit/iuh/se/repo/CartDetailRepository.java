package fit.iuh.se.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import fit.iuh.se.model.CartDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartDetailRepository extends ReactiveMongoRepository<CartDetail, String>{
	Flux<CartDetail> findByCustomerIdOrderByCreatedAt(String customerId);
	Mono<Void> deleteByCustomerId(String customerId);
}
