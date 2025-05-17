package iuh.fit.se.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class VendorResponseDTO extends CustomerResponseDTO {
	String shopName;
	String shopDescription;
}
