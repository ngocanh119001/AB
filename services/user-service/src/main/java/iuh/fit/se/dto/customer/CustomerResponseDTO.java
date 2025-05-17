package iuh.fit.se.dto.customer;

import java.util.List;

import iuh.fit.se.dto.user.UserResponseDTO;
import iuh.fit.se.model.Address;
import iuh.fit.se.model.BankAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class CustomerResponseDTO extends UserResponseDTO {
	int orderCount;
	List<Address> address;
	BankAccount bank;
}
