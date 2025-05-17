package fit.iuh.se.dto.cartDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CartDetailCreateRequestDto(
		
		@NotBlank(message = "productId required") String productId,
		@NotBlank(message = "customer required") String customerId,
		@Min(value = 1, message = "quantity must at least 1") Integer quantity,
		String firstCategory,
		String secondCategory
		) {
	
}
