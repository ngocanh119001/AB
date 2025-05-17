package iuh.fit.se.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import iuh.fit.se.dto.vendor.VendorResponseDTO;
import iuh.fit.se.dto.vendor.VendorShopUpdateDTO;
import iuh.fit.se.model.Vendor;

@Mapper(componentModel = "spring")
public interface VendorMapper {
	Vendor fromCustomerToVendor(iuh.fit.se.model.Customer customer);

	@Mapping(target = "fullName", expression = "java(vendor.getFirstName() + \" \" + vendor.getLastName())")
	VendorResponseDTO toVendorResponseDTO(Vendor vendor);
	
	Vendor updateVendorShop(@MappingTarget Vendor vendor, VendorShopUpdateDTO dto);
}
