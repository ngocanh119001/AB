package iuh.fit.se.service;

import reactor.core.publisher.Mono;

public interface StatisticUpdateService {
	
	public Mono<Void> updateVisitedProduct(String id);
	
}
