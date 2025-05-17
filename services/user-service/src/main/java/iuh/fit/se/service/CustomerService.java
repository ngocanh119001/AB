package iuh.fit.se.service;

import java.util.List;

import iuh.fit.se.dto.customer.AddressRequestDTO;
import iuh.fit.se.dto.customer.BankAccountRequestDto;
import iuh.fit.se.dto.customer.CustomerResponseDTO;
import iuh.fit.se.model.BankAccount;
import iuh.fit.se.model.Customer;


public interface CustomerService {
//	boolean deleteCustomer(String id, String realmId);
	
	List<Customer> getAllCustomers();
	CustomerResponseDTO getCustomerById(String id);
	Customer addCustomer(Customer customer);
	boolean addBankAccount(String id, BankAccountRequestDto bankAccount);
	CustomerResponseDTO createAddress(String id, AddressRequestDTO addressDto);
	CustomerResponseDTO updateAddress(String id, String addressId, AddressRequestDTO addressDto);
	CustomerResponseDTO deleteAddress(String id, String addressId);
}
