package fit.edu.se.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

//mongodb

@Document(collection = "vouchers")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher implements Serializable {
	
	private static final long serialVersionUID = 5027259941516372362L;
	@Id
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
