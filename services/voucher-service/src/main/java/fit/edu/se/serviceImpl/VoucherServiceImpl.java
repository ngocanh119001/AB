package fit.edu.se.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fit.edu.se.dto.VoucherCreateRequestDto;
import fit.edu.se.dto.VoucherResponseDto;
import fit.edu.se.dto.VoucherUpdateRequestDto;
import fit.edu.se.entity.Voucher;
import fit.edu.se.mapper.VoucherMapper;
import fit.edu.se.repository.VoucherRepository;
import fit.edu.se.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService{
	VoucherRepository voucherRepository;
//	UserClient userClient;
	VoucherMapper voucherMapper;
	
	@Override
	public VoucherResponseDto createVoucher(VoucherCreateRequestDto voucherCreateRequestDto) {
		// TODO Auto-generated method stub
		Voucher voucher = voucherMapper.toVoucher(voucherCreateRequestDto);
		voucher.setVoucherId(null); // Set the ID to null to let MongoDB generate it
		voucher = voucherRepository.save(voucher);
		VoucherResponseDto voucherResponseDto = voucherMapper.toVoucherResponseDto(voucher);
		return voucherResponseDto;
	}

	@Override
	@Cacheable(value = "vouchers", key = "#voucherId")
	public VoucherResponseDto getVoucherById(String voucherId) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(voucherId)
			.map(voucherMapper::toVoucherResponseDto)
			
			.orElseThrow(() -> new RuntimeException("Voucher not found"));

		
	}

	
	@Override
	@CachePut(value = "vouchers", key = "#voucherId")
	public VoucherResponseDto updateVoucher(String voucherId,
			VoucherUpdateRequestDto voucherCreateRequestDto) {
		// TODO Auto-generated method stub
		Optional<Voucher> voucher = voucherRepository.findById(voucherId);
		if (voucher.isPresent()) {
			Voucher voucherToUpdate = voucherMapper.toVoucher(voucher.get(), voucherCreateRequestDto);
			voucherToUpdate.setVoucherId(voucherId);
			voucherRepository.save(voucherToUpdate);
			VoucherResponseDto voucherResponseDto = voucherMapper.toVoucherResponseDto(voucherToUpdate);
			return voucherResponseDto;
		}
		else {
			throw new RuntimeException("Voucher not found");
		}
	}

	@Override
	@CacheEvict(value = "vouchers", key = "#voucherId")
	public VoucherResponseDto deleteVoucher(String voucherId) {
		// TODO Auto-generated method stub
		Optional<Voucher> voucher = voucherRepository.findById(voucherId);
		if (voucher.isPresent()) {
			voucherRepository.delete(voucher.get());
			VoucherResponseDto voucherResponseDto = voucherMapper.toVoucherResponseDto(voucher.get());
			return voucherResponseDto;
		}
		else {
			throw new RuntimeException("Voucher not found");
		}
	}

	@Override
	public Page<VoucherResponseDto> getAllVouchers(int page, int size) {
		Page<Voucher> vouchers = voucherRepository.findAll(PageRequest.of(page, size));
		Page<VoucherResponseDto> voucherResponseDtos = vouchers.map(voucherMapper::toVoucherResponseDto);
		return voucherResponseDtos;
	}

	@Override
	public Page<VoucherResponseDto> getVouchersByVendorId(String vendorId, int page, int size) {
		Page<Voucher> vouchers = voucherRepository.findByVendorId(vendorId, PageRequest.of(page, size));
		Page<VoucherResponseDto> voucherResponseDtos = vouchers.map(voucherMapper::toVoucherResponseDto);
		return voucherResponseDtos;
	}

	@Override
	public VoucherResponseDto increaseUsesCount(String voucherId, int usesCount) {
		// TODO Auto-generated method stub
		Voucher orElseThrow = voucherRepository.findById(voucherId)
			.map(voucher -> {
				if (voucher.getUsesCount() + usesCount < 0) {
					throw new RuntimeException("Uses count cannot be negative");
				}
				voucher.setUsesCount(voucher.getUsesCount() + usesCount);
				return voucherRepository.save(voucher);
			})
			.orElseThrow(() -> new RuntimeException("Voucher not found"));
		return voucherMapper.toVoucherResponseDto(orElseThrow);
	}

	@Override
	public List<VoucherResponseDto> getVouchersByVendorIdAvailable(String vendorId) {
		// TODO Auto-generated method stub
		List<Voucher> vouchers = voucherRepository.findByVendorId(vendorId);
		List<VoucherResponseDto> list = vouchers.stream()
			.filter(voucher -> voucher.getEndDate().isAfter(java.time.LocalDate.now()))
			.filter(voucher -> voucher.getStartDate().isBefore(java.time.LocalDate.now()))
			.filter(voucher -> voucher.getUsesCount() > 0)
			.map(voucherMapper::toVoucherResponseDto)
			.toList();
		return list;
	}
	

}
