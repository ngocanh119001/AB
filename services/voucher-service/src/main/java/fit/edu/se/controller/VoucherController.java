package fit.edu.se.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fit.edu.se.dto.VoucherCreateRequestDto;
import fit.edu.se.dto.VoucherResponseDto;
import fit.edu.se.dto.VoucherUpdateRequestDto;
import fit.edu.se.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {

	VoucherService voucherService;
	
	@GetMapping("/{voucherId}")
	public ResponseEntity<VoucherResponseDto> getVoucherById(@PathVariable String voucherId) {
		return ResponseEntity.ok(voucherService.getVoucherById(voucherId));
	}
	
	@GetMapping("/vendor/{vendorId}")
	public ResponseEntity<Page<VoucherResponseDto>> getVouchersByVendorId(
			@PathVariable String vendorId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<VoucherResponseDto> list = voucherService.getVouchersByVendorId(vendorId,page, size);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/vendor/{vendorId}/available")
	public ResponseEntity<List<VoucherResponseDto>> getVouchersByVendorIdAvailable(
			@PathVariable String vendorId) {
		List<VoucherResponseDto> list = voucherService.getVouchersByVendorIdAvailable(vendorId);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping
	public ResponseEntity<Page<VoucherResponseDto>> getAllVouchers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(voucherService.getAllVouchers(page, size));
	}
	
	@PostMapping
	public ResponseEntity<VoucherResponseDto> createVoucher(
			@RequestBody @Valid VoucherCreateRequestDto voucherCreateRequestDto
	) {
		return ResponseEntity.ok(voucherService.createVoucher(voucherCreateRequestDto));
	}
	
	@PutMapping("/{voucherId}")	
	public ResponseEntity<VoucherResponseDto> updateVoucher(
			@PathVariable String voucherId,
			@RequestBody @Valid VoucherUpdateRequestDto voucherUpdateRequestDto
	) {
		return ResponseEntity.ok(voucherService.updateVoucher(voucherId, voucherUpdateRequestDto));
	}
	
	@DeleteMapping("/{voucherId}")
	public ResponseEntity<VoucherResponseDto> deleteVoucher(
			@PathVariable String voucherId
	) {
		return ResponseEntity.ok(voucherService.deleteVoucher(voucherId));
	}
	
	@PutMapping("/{voucherId}/increase-uses-count")
	public ResponseEntity<VoucherResponseDto> increaseUsesCount(
			@PathVariable String voucherId,
			@RequestParam Integer usesCount
	) {
		return ResponseEntity.ok(voucherService.increaseUsesCount(voucherId, usesCount));
	}
	
	
	
}
