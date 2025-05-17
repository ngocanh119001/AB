package fit.edu.se.dto.order;

public record OrderDetailRequestDto(
		String productId,
		Integer quantity,
		String firstCategory,
		String secondCategory
		) {

}
