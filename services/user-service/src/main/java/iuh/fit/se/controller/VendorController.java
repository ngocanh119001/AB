package iuh.fit.se.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.fit.se.dto.vendor.VendorResponseDTO;
import iuh.fit.se.dto.vendor.VendorShopUpdateDTO;
import iuh.fit.se.model.Vendor;
import iuh.fit.se.serviceImpl.VendorServiceImpl;
import iuh.fit.se.util.JwtConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users/vendors")
@AllArgsConstructor
@Slf4j
public class VendorController {

    private final VendorServiceImpl vendorService;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }
    
    @PostMapping("/{id}/become-vendor")
    public ResponseEntity<Boolean> becomeVendor(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
		boolean test = vendorService.becomeVendor(id, JwtConverter.extractRealmIdFromJwt(jwt));
		return ResponseEntity.ok(test);
	}
    
    @PutMapping("/{id}/shop-update")
    public ResponseEntity<VendorResponseDTO> updateShop(@PathVariable String id, @RequestBody VendorShopUpdateDTO vendor) {
		
		return ResponseEntity.ok(vendorService.updateShop(id, vendor));
	}

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> getVendorById(@PathVariable String id) {
    	return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorService.createVendor(vendor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Optional<Vendor> updated = vendorService.updateVendor(id, vendor);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable String id) {
        return vendorService.deleteVendor(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}