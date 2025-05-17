package fit.iuh.se.dto.cartDetail;

public record CartDetailUpdateRequestDto(
		String firstCategory,
		String secondCategory,
		Integer quantity
		) {
	
}
