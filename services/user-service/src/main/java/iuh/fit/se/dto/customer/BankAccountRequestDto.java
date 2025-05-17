package iuh.fit.se.dto.customer;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BankAccountRequestDto(
		@NotBlank(message = "Bank name is required")
		String bankNumber, 
		@NotNull(message = "due date is required")
		LocalDate dueDate, 
		@NotBlank(message="Owner name is required")
		String ownerName,
		String address,
		Integer zipCode
		) {

}
