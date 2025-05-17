package fit.edu.se.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import fit.edu.se.dto.VoucherCreateRequestDto;
import fit.edu.se.dto.VoucherResponseDto;
import fit.edu.se.dto.VoucherUpdateRequestDto;

public interface VoucherService {
	
	VoucherResponseDto createVoucher(VoucherCreateRequestDto voucherCreateRequestDto);
	VoucherResponseDto getVoucherById(String voucherId);
	VoucherResponseDto updateVoucher(String voucherId, VoucherUpdateRequestDto voucherCreateRequestDto);
	VoucherResponseDto deleteVoucher(String voucherId);
	Page<VoucherResponseDto> getVouchersByVendorId(String vendorId, int page, int size);
	Page<VoucherResponseDto> getAllVouchers(int page, int size);
	VoucherResponseDto increaseUsesCount(String voucherId, int usesCount);
	List<VoucherResponseDto> getVouchersByVendorIdAvailable(String vendorId);
	
}
