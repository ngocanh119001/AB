package iuh.fit.se.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Service;

import iuh.fit.se.service.StatisticUpdateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class StatisticUpdateServiceImpl implements StatisticUpdateService {

	private final String VISITED_PRODUCT = "product:visited:";
	private final RScoredSortedSetReactive<String> scoredProduct;
	private final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
	private final RedissonReactiveClient client;
	
	public StatisticUpdateServiceImpl(RedissonReactiveClient client) {
		scoredProduct = client.getScoredSortedSet(VISITED_PRODUCT);
		this.client = client;
	}
	
	@PostConstruct
	public void init() {
		// TODO Auto-generated method stub
		log.info("Starting @PostConstruct in StatisticUpdateServiceImpl");
		sink.asFlux().buffer(Duration.ofSeconds(3)).map(ls -> ls.stream().collect(
				Collectors.groupingBy(
						id -> id,
						Collectors.counting()
				)))
		.flatMap(this::updateBatch)
		.subscribe();
	}
	
	@Override
	public Mono<Void> updateVisitedProduct(String id) {
		// TODO Auto-generated method stub
		return sink.tryEmitNext(id).isSuccess() ? Mono.empty() : Mono.error(new RuntimeException("Error"));
	}
	
	private Mono<Void> updateBatch(Map<String,Long> values) {
		RBatchReactive batch = client.createBatch(BatchOptions.defaults());
		String formatDated = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
		RScoredSortedSetReactive<String> scoredSortedSet = 
				batch.getScoredSortedSet(VISITED_PRODUCT + formatDated);
		return Flux.fromIterable(values.entrySet())
			.map((entry) -> scoredSortedSet.addScore(entry.getKey(),entry.getValue()))
			.then(batch.execute())
			.then()
			.doOnError(e -> System.err.println("Error executing batch: " + e.getMessage()))
	        .onErrorResume(e -> Mono.empty());
	}

}
