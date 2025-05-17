package iuh.fit.se.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import iuh.fit.se.client.ProductClient;
import iuh.fit.se.client.UserClient;
import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.ReviewCreateRequestDto;
import iuh.fit.se.dto.ReviewResponseDto;
import iuh.fit.se.dto.ReviewUpdateDto;
import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.mapper.ReviewMapper;
import iuh.fit.se.model.Review;
import iuh.fit.se.repo.ReviewRepository;
import iuh.fit.se.repo.ReviewRepositoryCustom;
import iuh.fit.se.serivce.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

	ReviewRepository reviewRepository;
	ReviewRepositoryCustom reviewRepositoryCustom;
	ReviewMapper reviewMapper;
	ProductClient productClient;
	UserClient userClient;

	@Override
	public Mono<ReviewResponseDto> createReview(ReviewCreateRequestDto reviewCreateRequestDto) {
		// TODO Auto-generated method stub
		return Mono.just(reviewCreateRequestDto).map(reviewMapper::fromReviewCreateRequestToEntity).map(review -> {
			review.setReviewId(UUID.randomUUID().toString());
			return review;
		}).flatMap(review -> userClient.getCustomerById(review.getCustomerId()).thenReturn(review))
				.flatMap(reviewRepository::save).flatMap(
						review -> Mono
								.zip(fetchReviewWithCustomer(review),
										reviewRepositoryCustom.findAverageRatingByProductId(review.getProductId())
												.flatMap(avgRating -> productClient.callUpdateAvgRating(
														reviewCreateRequestDto.getProductId(), avgRating)))
								.map(tuple -> tuple.getT1()));
	}

	@Override
	public Mono<CustomPage<ReviewResponseDto>> getReviewsByProductId(String productId, int page, int size) {
		// TODO Auto-generated method stub
		int skip = page * size;
		if (skip < 0) {
			skip = 0;
		}
		Mono<Long> countMono = reviewRepository.countByProductId(productId);
		Flux<ReviewResponseDto> reviewsFlux = reviewRepository.findByProductId(productId).skip(skip).take(size)
				.flatMap(review -> {
					// Fetch customer details for each review
					Mono<UserResponseDTO> customerResponse = userClient.getUserById(review.getCustomerId());
					ReviewResponseDto reviewResponseDto = reviewMapper.fromEntityToReviewResponse(review);
					return customerResponse.map(user -> {
						reviewResponseDto.setCustomer(user);
						return reviewResponseDto;
					});
				})

		;
		// Fetch customer details for each review

		return Mono.zip(countMono, reviewsFlux.collectList()).map(tuple -> {
			long total = tuple.getT1();
			List<ReviewResponseDto> content = tuple.getT2();
			int totalPages = (int) Math.ceil((double) total / size);
			return new CustomPage<>(content, page, size, total, totalPages);
		});
	}

	@Override
	public Mono<Void> deleteReview(String reviewId) {
		// TODO Auto-generated method stub
		return reviewRepository.deleteById(reviewId)
				.doOnSuccess(unused -> log.info("Deleted review with ID: {}", reviewId))
				.doOnError(error -> log.error("Error deleting review with ID: {}", reviewId, error));
	}

	@Override
	public Mono<ReviewResponseDto> updateReview(String reviewId, ReviewUpdateDto reviewCreateRequestDto) {
		// TODO Auto-generated method stub
		return reviewRepository.findById(reviewId).flatMap(review -> {
			review.setRating(reviewCreateRequestDto.rating());
			review.setComment(reviewCreateRequestDto.comment());
			return reviewRepository.save(review);
		}).flatMap(review -> Mono
				.zip(fetchReviewWithCustomer(review),
						reviewRepositoryCustom.findAverageRatingByProductId(review.getProductId()).flatMap(
								avgRating -> productClient.callUpdateAvgRating(review.getProductId(), avgRating)))
				.map(tuple -> tuple.getT1()))
				.doOnSuccess(updatedReview -> log.info("Updated review with ID: {}", reviewId))
				.doOnError(error -> log.error("Error updating review with ID: {}", reviewId, error));
	}

	@Override
	public Mono<ReviewResponseDto> getReviewById(String reviewId) {
		// TODO Auto-generated method stub
		return reviewRepository.findById(reviewId).switchIfEmpty(Mono.error(new RuntimeException("Review not found")))
				.flatMap(this::fetchReviewWithCustomer)
				.doOnSuccess(review -> log.info("Fetched review with ID: {}", reviewId))
				.doOnError(error -> log.error("Error fetching review with ID: {}", reviewId, error));
	}

	@Override
	public Mono<Double> getAverageRatingByProductId(String productId) {
		// TODO Auto-generated method stub
		return reviewRepositoryCustom.findAverageRatingByProductId(productId)
				.doOnSuccess(averageRating -> log.info("Fetched average rating for product ID: {}", productId))
				.doOnError(error -> log.error("Error fetching average rating for product ID: {}", productId, error));
	}

	private Mono<ReviewResponseDto> fetchReviewWithCustomer(Review review) {
		Mono<UserResponseDTO> customerResponse = userClient.getUserById(review.getCustomerId());
		return customerResponse.map(user -> {
			ReviewResponseDto reviewResponseDto = reviewMapper.fromEntityToReviewResponse(review);
			reviewResponseDto.setCustomer(user);
			return reviewResponseDto;
		});
	}
}
