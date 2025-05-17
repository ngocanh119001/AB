package iuh.fit.se.dto.customer;

import iuh.fit.se.model.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDTO(
		@NotBlank(message = "Recipient name is required") String recipientName,
		@NotBlank(message = "Recipient phone is required") String recipientPhone,
		@NotBlank(message = "Recipient address is required") String recipientAddress,
		@NotNull(message = "Address Type is required") AddressType addressType
		) {

}
