package fit.edu.se.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import fit.edu.se.dto.VoucherResponseDto;
import fit.edu.se.entity.Voucher;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
	
	@Mapping(target = "voucherId", ignore = true) // Ignore the id field in the target entity
	Voucher toVoucher(fit.edu.se.dto.VoucherCreateRequestDto voucherCreateRequestDto);

	@Mapping(target = "voucherId",ignore = true) // Map the id field from the source entity
	@Mapping(target = "vendorId",ignore = true) // Map the id field from the source entity
	Voucher toVoucher(@MappingTarget Voucher voucher, fit.edu.se.dto.VoucherUpdateRequestDto voucherUpdateRequestDto);
	
	VoucherResponseDto toVoucherResponseDto(Voucher voucher);
}
