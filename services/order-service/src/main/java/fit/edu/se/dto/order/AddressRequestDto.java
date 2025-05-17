package fit.edu.se.dto.order;

public record AddressRequestDto(
		String recipientName, 
		String recipientPhone, 
		String recipientAddress) {

}
