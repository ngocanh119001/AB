package iuh.fit.se.service;

import iuh.fit.se.dto.vendor.VendorResponseDTO;
import iuh.fit.se.dto.vendor.VendorShopUpdateDTO;
import iuh.fit.se.model.Vendor;
import java.util.List;
import java.util.Optional;

public interface VendorService {
    boolean becomeVendor(String customerId, String realmId);
    List<Vendor> getAllVendors();
    VendorResponseDTO getVendorById(String id);
    Vendor createVendor(Vendor vendor);
    Optional<Vendor> updateVendor(String id, Vendor vendor);
    boolean deleteVendor(String id);
    VendorResponseDTO updateShop(String userId, VendorShopUpdateDTO vendorShopUpdateDTO);
}