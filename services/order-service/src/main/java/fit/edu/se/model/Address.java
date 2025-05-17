package fit.edu.se.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address implements Persistable<String>{
	@Id
	String addressId;
	String recipientName;
	String recipientPhone;
	String recipientAddress;
	
	@Transient
    boolean isNew = false;
//	sẽ không có trường addressType do không cần thiết đối với orders

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return addressId;
	}
}
