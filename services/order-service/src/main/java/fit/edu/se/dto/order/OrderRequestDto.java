package fit.edu.se.dto.order;

import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto{
	@NotBlank(message = "Order ID cannot be null")
	String orderId;
	@NotBlank(message = "Customer ID cannot be null")
	String customerId;
	@NotBlank(message = "Vendor ID cannot be null")
	String vendorId;
	String notes;
	@NotNull(message = "Address cannot be null")
	AddressRequestDto deliveryAddress;
	List<OrderDetailRequestDto> orderDetails;
	String voucherId;
	Integer totalPrice;
}
