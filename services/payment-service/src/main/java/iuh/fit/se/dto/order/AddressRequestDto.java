package iuh.fit.se.dto.order;

public record AddressRequestDto(
		String recipientName, 
		String recipientPhone, 
		String recipientAddress) {

}
