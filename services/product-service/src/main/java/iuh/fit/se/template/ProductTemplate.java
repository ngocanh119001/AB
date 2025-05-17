package iuh.fit.se.template;

import java.time.Duration;

import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Component;

import iuh.fit.se.model.Product;
import iuh.fit.se.repo.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTemplate extends CacheTemplate<Product, String> {
	
	private final ProductRepository productRepository;
	private final String CACHE_NAME = "product:";
	private final RedissonReactiveClient client;
	
	
	@Override
	public Mono<Product> findByIdCache(String id) {
		// TODO Auto-generated method stub
		return client.getBucket(CACHE_NAME + id).get().cast(Product.class);
	}

	@Override
	public Mono<Product> findByIdDb(String id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id);
	}

	@Override
	public Mono<Product> saveCache(String id, Product item) {
		// TODO Auto-generated method stub
		return client.getBucket(CACHE_NAME + id).set(item, Duration.ofMinutes(10)).cast(Product.class);
	}

	@Override
	public Mono<Product> saveDb(String id, Product item) {
		// TODO Auto-generated method stub
		item.setProductId(id);
		return productRepository.save(item);
	}

	@Override
	public Mono<Void> deleteCache(String id) {
		// TODO Auto-generated method stub
		return client.getBucket(CACHE_NAME + id).delete().then();	
	}

	@Override
	public Mono<Void> deleteDb(String id) {
		// TODO Auto-generated method stub
		return productRepository.deleteById(id);
	}

}
