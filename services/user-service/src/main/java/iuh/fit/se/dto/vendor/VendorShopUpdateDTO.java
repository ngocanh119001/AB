package iuh.fit.se.dto.vendor;

import jakarta.validation.constraints.NotBlank;

public record VendorShopUpdateDTO
		(@NotBlank String shopName, 
		String shopDescription) {

}
