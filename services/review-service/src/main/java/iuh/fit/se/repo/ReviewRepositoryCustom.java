package iuh.fit.se.repo;

import reactor.core.publisher.Mono;

public interface ReviewRepositoryCustom {
	Mono<Double> findAverageRatingByProductId(String productId);
}
