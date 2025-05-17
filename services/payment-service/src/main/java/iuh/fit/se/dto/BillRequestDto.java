package iuh.fit.se.dto;


public record BillRequestDto(
		String id,
		String gateway,
		String transactionsDate,
		String code,
		String content,
		String transferType,
		Integer transferAmount,
		Integer accumulated,
		String subAccount,
		String referenceCode, 
		String description) {
	
}
