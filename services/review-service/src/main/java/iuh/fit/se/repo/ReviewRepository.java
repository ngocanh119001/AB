package iuh.fit.se.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import iuh.fit.se.model.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
	Flux<Review> findByProductId(String productId);
	Mono<Long> countByProductId(String productId);
}
