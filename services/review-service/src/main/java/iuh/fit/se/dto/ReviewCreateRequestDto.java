package iuh.fit.se.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewCreateRequestDto {
	@NotNull(message = "Rating cannot be null")
	@Min(value = 1, message = "Stars must be at least 1")
	@Max(value = 5, message = "Stars must be at most 5")
	Integer rating;
	String comment;
	@NotBlank(message = "Product ID cannot be blank")
	String productId;
	@NotBlank(message = "Customer ID cannot be blank")
	String customerId;
}
