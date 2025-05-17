package iuh.fit.se.dto;

import iuh.fit.se.dto.user.UserResponseDTO;
import lombok.experimental.FieldDefaults;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewResponseDto{
		String reviewId; 
		Integer rating;
		String comment; 
		String productId;
		UserResponseDTO customer;
}
