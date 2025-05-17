package iuh.fit.se.serivce;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.ReviewCreateRequestDto;
import iuh.fit.se.dto.ReviewResponseDto;
import iuh.fit.se.dto.ReviewUpdateDto;
import reactor.core.publisher.Mono;

public interface ReviewService {
	Mono<ReviewResponseDto> createReview(ReviewCreateRequestDto reviewCreateRequestDto);
	Mono<CustomPage<ReviewResponseDto>> getReviewsByProductId(String productId, int page, int size);
	Mono<Void> deleteReview(String reviewId);
	Mono<ReviewResponseDto> updateReview(String reviewId, ReviewUpdateDto reviewCreateRequestDto);
	Mono<ReviewResponseDto> getReviewById(String reviewId);
	Mono<Double> getAverageRatingByProductId(String productId);
	
}
