package iuh.fit.se.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.customer.AddressRequestDTO;
import iuh.fit.se.dto.customer.BankAccountRequestDto;
import iuh.fit.se.dto.customer.CustomerResponseDTO;
import iuh.fit.se.dto.user.UserUpdateDTO;
import iuh.fit.se.model.Customer;
import iuh.fit.se.service.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/customers")
@Validated
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ✅ Tạo mới Customer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    // ✅ Lấy tất cả Customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // ✅ Lấy 1 Customer theo ID
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable String id) {
    	CustomerResponseDTO customer = customerService.getCustomerById(id);
        if (customer != null) {
            return customer;
        }
        throw new RuntimeException("Customer not found with id " + id);
    }
    @PostMapping("/{id}/bank-account")
    public boolean addBankAccount(@PathVariable String id, @RequestBody BankAccountRequestDto bankAccount) {
		return customerService.addBankAccount(id, bankAccount);
	}
    @PostMapping("/{id}/address")
    public CustomerResponseDTO createAddress(@PathVariable String id, @RequestBody AddressRequestDTO addressDto) {
		return customerService.createAddress(id, addressDto);
	}
    
    @PutMapping("/{id}/address/{addressId}")
	public CustomerResponseDTO updateAddress(
			@PathVariable String id, 
			@PathVariable String addressId,
			@RequestBody AddressRequestDTO addressDto
			) {
		return customerService.updateAddress(id, addressId, addressDto);
	}
	
	@DeleteMapping("/{id}/address/{addressId}")
	public CustomerResponseDTO deleteAddress(@PathVariable String id, @PathVariable String addressId) {
		return customerService.deleteAddress(id, addressId);
	}
	
}
