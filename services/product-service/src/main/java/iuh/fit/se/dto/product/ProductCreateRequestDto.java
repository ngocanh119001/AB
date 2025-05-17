package iuh.fit.se.dto.product;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.codec.multipart.FilePart;

import iuh.fit.se.constraint.FileSizeList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateRequestDto{
	@NotBlank(message = "Tên sản phẩm không được để trống")
	private String productName;
	@NotNull(message = "Giá sản phẩm không được để trống")
	private Double price;
	@NotNull(message = "Số lượng sản phẩm không được để trống")
	@PositiveOrZero(message = "Số lượng sản phẩm không được âm")
	private Integer stock;
	private String description;
	
	@NotBlank(message = "Vendor ID không được để trống")
	private String vendorId;
	private String brand;
	
	private String firstCategoryName;
	private String secondCategoryName;
	
	private List<String> firstCategories;
	private List<String> secondCategories;
//	@Size(max = 5 * 1024 * 1024, message = "Kích thước tệp không được vượt quá 5MB")
	@NotNull(message = "Ảnh bìa không được để trống")
	private FilePart coverImage;
	
	@Size(min = 3, max = 9, message = "Số lượng ảnh phải từ 3 đến 9")
	
	private List<FilePart> images;
//	@Size(max = 10 * 1024 * 1024, message = "Kích thước video không được vượt quá 10MB")
	@NotNull(message = "Video không được để trống")
	private FilePart video;
	
	private boolean isNew;
}
