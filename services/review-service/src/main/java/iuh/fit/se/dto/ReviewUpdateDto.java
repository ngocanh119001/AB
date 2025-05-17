package iuh.fit.se.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewUpdateDto(
		@NotNull(message = "Rating cannot be null") 
		@Min(value = 1, message = "Stars must be at least 1") 
		@Max(value = 5, message = "Stars must be at most 5") 
		Integer rating,
		String comment
) {}
