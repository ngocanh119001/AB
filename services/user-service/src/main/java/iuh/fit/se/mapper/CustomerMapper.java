package iuh.fit.se.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import iuh.fit.se.dto.customer.AddressRequestDTO;
import iuh.fit.se.dto.customer.BankAccountRequestDto;
import iuh.fit.se.dto.customer.CustomerResponseDTO;
import iuh.fit.se.dto.user.UserUpdateDTO;
import iuh.fit.se.model.Address;
import iuh.fit.se.model.BankAccount;
import iuh.fit.se.model.Customer;
import iuh.fit.se.model.UserRole;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	Customer updateCustomer(@MappingTarget Customer customer, UserUpdateDTO dto);
	
	@Mapping(target = "fullName", expression = "java(customer.getFirstName() + \" \" + customer.getLastName())")
	CustomerResponseDTO toCustomerResponseDTO(Customer customer);
	
	Address toAddress(AddressRequestDTO dto);
	
	BankAccount toBankAccount(BankAccountRequestDto bankAccountRequestDto);
	
}
