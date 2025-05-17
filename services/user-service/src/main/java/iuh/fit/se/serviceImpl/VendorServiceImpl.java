package iuh.fit.se.serviceImpl;

import iuh.fit.se.dto.vendor.VendorResponseDTO;
import iuh.fit.se.dto.vendor.VendorShopUpdateDTO;
import iuh.fit.se.mapper.VendorMapper;
import iuh.fit.se.model.*;
import iuh.fit.se.repository.*;
import iuh.fit.se.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final VendorMapper vendorMapper;
    private final Keycloak keycloak;

    @Override
    @Transactional
    public boolean becomeVendor(String customerId, String realmId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    Vendor vendor = vendorMapper.fromCustomerToVendor(customer);
                    assignVendorRole(customerId, realmId);
                    customerRepository.delete(customer);
                    vendor.addRole(new UserRole(realmId, RealmRoles.VENDOR.toString()));
                    vendorRepository.save(vendor);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public VendorResponseDTO getVendorById(String id) {
        return vendorRepository
        		.findById(id)
        		.map(vendorMapper::toVendorResponseDTO)
        		.orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    @Override
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public Optional<Vendor> updateVendor(String id, Vendor vendor) {
        return vendorRepository.findById(id)
                .map(existing -> vendorRepository.save(vendor));
    }

    @Override
    public boolean deleteVendor(String id) {
        if (vendorRepository.existsById(id)) {
            vendorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void assignVendorRole(String userId, String realmId) {
        ensureRoleExists(realmId, RealmRoles.VENDOR);
        
        userRepository.findById(userId).ifPresent(user -> {
            boolean hasVendorRole = user.getRoles().stream()
                    .anyMatch(role -> RealmRoles.VENDOR.toString().equals(role.getRoleName()));

            if (!hasVendorRole) {
                RoleRepresentation vendorRole = keycloak.realm(realmId)
                        .roles()
                        .get(RealmRoles.VENDOR.toString())
                        .toRepresentation();
                
                keycloak.realm(realmId)
                        .users()
                        .get(userId)
                        .roles()
                        .realmLevel()
                        .add(List.of(vendorRole));
            }
        });
    }

    private void ensureRoleExists(String realmId, RealmRoles role) {
        try {
            keycloak.realm(realmId).roles().get(role.toString()).toRepresentation();
        } catch (Exception e) {
            RoleRepresentation newRole = new RoleRepresentation();
            newRole.setName(role.toString());
            keycloak.realm(realmId).roles().create(newRole);
        }
    }

	@Override
	public VendorResponseDTO updateShop(String userId, VendorShopUpdateDTO vendorShopUpdateDTO) {
		// TODO Auto-generated method stub
		Optional<Vendor> existingVendor = vendorRepository.findById(userId);
		if (existingVendor.isPresent()) {
			Vendor vendor = existingVendor.get();
			vendorMapper.updateVendorShop(vendor, vendorShopUpdateDTO);
			vendorRepository.save(vendor);
			return vendorMapper.toVendorResponseDTO(vendor);
		} else {
			throw new RuntimeException("Vendor not found with id " + userId);
		}
	}
}