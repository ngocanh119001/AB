package iuh.fit.se.dto.user;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Quan trọng cho Set
public class Address {
	@Id
	@EqualsAndHashCode.Include
	String addressId;
	String recipientName;
	String recipientPhone;
	String recipientAddress;
	String addressType;

}
