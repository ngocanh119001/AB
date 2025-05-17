package iuh.fit.se.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.CustomPage;
import iuh.fit.se.dto.ReviewCreateRequestDto;
import iuh.fit.se.dto.ReviewResponseDto;
import iuh.fit.se.dto.ReviewUpdateDto;
import iuh.fit.se.serivce.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public Mono<ResponseEntity<ReviewResponseDto>> createReview(
			@Valid @RequestBody ReviewCreateRequestDto reviewRequest) {
		return reviewService.createReview(reviewRequest)
				.map(reviewResponse -> ResponseEntity.ok(reviewResponse));
	}
	
//	code giúp tôi tìm kiếm phân trang trong danh sách đánh giá dựa trên id sản phẩm
	@GetMapping("/product/{productId}")
	public Mono<ResponseEntity<CustomPage<ReviewResponseDto>>> getReviewsByProductId(
			@PathVariable String productId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return reviewService.getReviewsByProductId(productId, page, size)
				.map(reviews -> ResponseEntity.ok(reviews));
	}
	
	@GetMapping("/product/{productId}/avg-rating")
	public Mono<ResponseEntity<Double>> getAverageRatingByProductId(@PathVariable String productId) {
		return reviewService.getAverageRatingByProductId(productId)
				.map(averageRating -> ResponseEntity.ok(averageRating));
	}
	@DeleteMapping("/{reviewId}")
	public Mono<ResponseEntity<Void>> deleteReview(@PathVariable String reviewId) {
		return reviewService.deleteReview(reviewId)
				.then(Mono.just(ResponseEntity.noContent().build()));
	}
	@PutMapping("/{reviewId}")
	public Mono<ResponseEntity<ReviewResponseDto>> updateReview(
			@PathVariable String reviewId,
			@Valid @RequestBody ReviewUpdateDto reviewRequest) {
		return reviewService.updateReview(reviewId, reviewRequest)
				.map(reviewResponse -> ResponseEntity.ok(reviewResponse));
	}
	
	
	@GetMapping("/{reviewId}")
	public Mono<ResponseEntity<ReviewResponseDto>> getReviewById(@PathVariable String reviewId) {
		return reviewService.getReviewById(reviewId)
				.map(reviewResponse -> ResponseEntity.ok(reviewResponse));
	}
}
