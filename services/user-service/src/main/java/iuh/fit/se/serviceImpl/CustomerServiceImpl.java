package iuh.fit.se.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import iuh.fit.se.dto.customer.AddressRequestDTO;
import iuh.fit.se.dto.customer.BankAccountRequestDto;
import iuh.fit.se.dto.customer.CustomerResponseDTO;
import iuh.fit.se.dto.keycloak.KeycloakDeleteUserEvent;
import iuh.fit.se.dto.keycloak.KeycloakUserEvent;
import iuh.fit.se.mapper.CustomerMapper;
import iuh.fit.se.mapper.UserRepresentationMapper;
import iuh.fit.se.model.Address;
import iuh.fit.se.model.AddressType;
import iuh.fit.se.model.BankAccount;
import iuh.fit.se.model.Customer;
import iuh.fit.se.model.UserRole;
import iuh.fit.se.repository.CustomerRepository;
import iuh.fit.se.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService{
	private final Keycloak keycloak;
    private final ObjectMapper objectMapper;
    
    private final CustomerMapper customerMapper;
    private final UserRepresentationMapper userRepresentationMapper;
    private final CustomerRepository customerRepository;

    @RabbitListener(queues = "user-events")
    private void handleKeycloakEvent(String message) {
    	log.info("Received message: {}", message);
        try {
            try {
                KeycloakUserEvent event = objectMapper.readValue(message, KeycloakUserEvent.class);
                if ("REGISTER".equals(event.getEventType()) || "UPDATE_PROFILE".equals(event.getEventType()) || 
                    "UPDATE_EMAIL".equals(event.getEventType()) || "CREATE".equals(event.getEventType()) || 
                    "UPDATE".equals(event.getEventType())) {
                    // Lưu hoặc cập nhật user-service DB
                	
                	List<UserRole> userRoles = new ArrayList<>();	
 
                    log.info("realm roles: {}", event.getRealmRoles());
                    
                    // Lưu realm roles
                    if (event.getRealmRoles() != null) {
                    	// Xóa tất cả realm roles hiện tại
                        event.getRealmRoles().forEach(role -> {
                        	UserRole userRole = new UserRole(event.getRealmId(), role);
                        	userRoles.add(userRole);	
                        });
                    }
                    Customer user = Customer.builder()
                			.userId(event.getUserId())
                			.clientId(event.getClientId())
                			.username(event.getUsername())
                			.firstName(event.getFirstName())
                			.lastName(event.getLastName())
                			.email(event.getEmail())
                			.phoneNumber(event.getPhone())
                			.roles(userRoles)
                			.createdAt(LocalDateTime.now())
                			.build();
                    log.info("user: {}", user);
                    customerRepository.save(user);
                }
                else if ("DELETE".equals(event.getEventType())) {
					// Xóa user và roles trong user-service DB
                	customerRepository.deleteById(event.getUserId());
				}
            } catch (Exception e) {
                KeycloakDeleteUserEvent deleteEvent = objectMapper.readValue(message, KeycloakDeleteUserEvent.class);
                if ("DELETE".equals(deleteEvent.getEventType())) {
                    // Xóa user và roles trong user-service DB
                	customerRepository.deleteById(deleteEvent.getUserId());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process Keycloak event: " + e.getMessage());
            // TODO: Thêm retry logic
        }
    }
    

    // Thêm khách hàng mới
    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    

    // Lấy tất cả khách hàng
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll(); // MongoRepository trả về List
    }

    // Lấy customer theo ID
    @Override
    public CustomerResponseDTO getCustomerById(String id) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
			Customer customer = existingCustomer.get();
			return customerMapper.toCustomerResponseDTO(customer);
		} else {
			throw new RuntimeException("Customer not found with id " + id);
		}
    }

	@Override
	@Transactional
	public boolean addBankAccount(String id, BankAccountRequestDto bankAccount) {
		// TODO Auto-generated method stub
		BankAccount bankAccountEntity = customerMapper.toBankAccount(bankAccount);
		Optional<Customer> existingCustomer = customerRepository.findById(id);
		if (existingCustomer.isPresent()) {
			Customer customer = existingCustomer.get();
			customer.setBank(bankAccountEntity);
			customerRepository.save(customer);
			return true;
		}
		else {
			throw new RuntimeException("Customer not found with id " + id);
		}
	}


	@Override
	@Transactional
	public CustomerResponseDTO updateAddress(String id, String addressId ,AddressRequestDTO addressDto) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Address address = customerMapper.toAddress(addressDto);
			address.setAddressId(addressId);
			customer.updateAddress(address);
			customerRepository.save(customer);
			return customerMapper.toCustomerResponseDTO(customer);
		} else {
			throw new RuntimeException("Customer not found with id " + id);
		}
	}
	
	@Override
	@Transactional
	public CustomerResponseDTO createAddress(String id, AddressRequestDTO addressDto) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Address address = customerMapper.toAddress(addressDto);
			address.setAddressId(UUID.randomUUID().toString());
			customer.addAddress(address);
			if (addressDto.addressType()== null) {
				address.setAddressType(AddressType.NORMAL);
			}
			if (addressDto.addressType() == AddressType.DEFAULT) {
				customer.getAddress().forEach(a -> {
					if (a.getAddressType() == AddressType.DEFAULT) {
						a.setAddressType(AddressType.NORMAL);
					}
				});
			}
			customerRepository.save(customer);
			return customerMapper.toCustomerResponseDTO(customer);
		} else {
			throw new RuntimeException("Customer not found with id " + id);
		}
	}

	@Override
	@Transactional
	public CustomerResponseDTO deleteAddress(String id, String addressId) {
		// TODO Auto-generated method stub
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.removeAddress(addressId);
			customerRepository.save(customer);
			return customerMapper.toCustomerResponseDTO(customer);
		} else {
			throw new RuntimeException("Customer not found with id " + id);
		}
	}





}
