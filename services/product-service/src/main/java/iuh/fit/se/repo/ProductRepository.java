package iuh.fit.se.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import iuh.fit.se.model.Product;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String>{
	Flux<Product> findByProductNameContainingIgnoreCase(String productName);
	
}
