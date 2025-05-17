package iuh.fit.se.dto.product;

import org.hibernate.validator.constraints.Range;

public record ProductUpdateAvgRatingRequestDto(
	@Range(min = 1, max = 5, message = "AvgRating must be between 1 and 5")
		Double avgRating)
{}
