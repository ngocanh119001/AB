package fit.edu.se.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fit.edu.se.dto.order.AddressRequestDto;
import fit.edu.se.dto.order.AddressResponseDto;
import fit.edu.se.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	@Mapping(target = "addressId", ignore = true)
	Address fromAddressRequestDto(AddressRequestDto addressRequestDto);
	
	AddressResponseDto toAddressResponseDto(Address address);
}
