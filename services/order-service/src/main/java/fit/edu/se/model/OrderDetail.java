package fit.edu.se.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table("order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
	@Id
	String orderDetailId;
	String orderId;
	String productId;
	Integer quantity;
	double price;
	String productName;
	String productImage;
	String firstCategory;
	String secondCategory;
	
	// Constructor, getters, setters, etc.
	// ...
}
