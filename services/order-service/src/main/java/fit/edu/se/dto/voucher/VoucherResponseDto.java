package fit.edu.se.dto.voucher;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponseDto {
	private String voucherId;
	private String voucherName;
	private String vendorId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double minPriceRequired;
	private Integer usesCount;
	private VoucherEnum voucherType;
	private Double percentDiscount;
	private Double valueDiscount;
}
