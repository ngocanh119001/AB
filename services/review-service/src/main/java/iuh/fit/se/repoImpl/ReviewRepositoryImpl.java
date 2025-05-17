package iuh.fit.se.repoImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import iuh.fit.se.repo.ReviewRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Double> findAverageRatingByProductId(String productId) {
        MatchOperation match = Aggregation.match(Criteria.where("productId").is(productId));
        GroupOperation group = Aggregation.group("productId").avg("rating").as("averageRating");

        
        Aggregation aggregation = Aggregation.newAggregation(match, group);
//        log.info("Aggregation: {}", aggregation.toString());
        
        return mongoTemplate.aggregate(aggregation, "reviews", Map.class)
            .next()
            .map(result -> (Double) result.get("averageRating"))
            .defaultIfEmpty(1.0);
    }
}
