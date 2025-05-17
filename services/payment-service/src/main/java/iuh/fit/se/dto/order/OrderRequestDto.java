package iuh.fit.se.dto.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto{
	String orderId;
	String customerId;
	String vendorId;
	String notes;
	AddressRequestDto deliveryAddress;
	String voucherId;
	List<OrderDetailRequestDto> orderDetails;
	Integer totalPrice;
}