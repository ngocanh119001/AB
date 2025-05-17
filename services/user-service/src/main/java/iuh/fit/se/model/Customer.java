package iuh.fit.se.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("CUSTOMER") // Quan trọng!
@SuperBuilder
@ToString(callSuper = true)
public class Customer extends User {

	@Builder.Default
	private int orderCount = 0;
	private Set<Address> address;
	private BankAccount bank;

	public void addAddress(Address address) {
	    if (address == null) return;
	    
	    if (this.address == null) {
	        this.address = new HashSet<>();
	    }
	    
	    // Kiểm tra tồn tại bằng contains() O(1)
	    if (this.address.contains(address)) { 
	        throw new IllegalStateException("Address already exists: " + address.getAddressId());
	    }
	    this.address.add(address); // Thêm mới O(1)
	}

	public void updateAddress(Address address) {
	    if (address == null || this.address == null) return;
	    
	    // Kiểm tra tồn tại bằng contains() O(1)
	    if (!this.address.contains(address)) {
	        throw new IllegalStateException("Address not found: " + address.getAddressId());
	    }
	    this.address.remove(address); // Xóa O(1)
	    this.address.add(address); // Thêm lại O(1)
	}
	public void removeAddress(String addressId) {
		if (this.address != null) {
			Address tempAddress = new Address();
	        tempAddress.setAddressId(addressId);
	        this.address.remove(tempAddress);
		}
	}
}
