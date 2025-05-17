package fit.iuh.se.dto.cartDetail;

import fit.iuh.se.dto.product.ProductResponseDto;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CartDetailResponse {
	String cartDetailId;
	ProductResponseDto product;
	Integer quantity;
	Double totalPrice;
	String firstCategory;
	String secondCategory;
	
}
