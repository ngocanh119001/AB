package fit.edu.se.dto;

import java.time.LocalDate;

import fit.edu.se.entity.VoucherType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponseDto {
	String voucherId;
	String voucherName;
	String vendorId;
	
	LocalDate startDate;
	LocalDate endDate;
	Double minPriceRequired;
	Integer usesCount;
	VoucherType voucherType;
	Double percentDiscount;
	Double valueDiscount;
}
