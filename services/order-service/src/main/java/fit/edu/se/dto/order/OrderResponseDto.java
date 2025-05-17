package fit.edu.se.dto.order;

import java.time.LocalDateTime;

import fit.edu.se.dto.user.VendorResponseDto;
import fit.edu.se.model.OrderState;

@lombok.AllArgsConstructor
@lombok.Data
@lombok.NoArgsConstructor
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderResponseDto {
	String orderId;
//	cái này liên quan đến người
//	chỉ lấy tên người
	String customerId;
//	chỉ lên tên người
	VendorResponseDto vendor;
	String notes;
	LocalDateTime estimatedDeliveryTime;
	AddressResponseDto deliveryAddress;
	OrderState orderState;
	Integer totalPrice;
	String voucherId;
//	null khi truy vấn danh sách
	java.util.List<OrderDetailResponseDto> orderDetails;
	LocalDateTime createdAt;
	
	
}
