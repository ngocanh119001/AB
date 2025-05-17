package iuh.fit.se.dto.product;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.codec.multipart.FilePart;

import iuh.fit.se.constraint.FileSizeList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequestDto{
	
	@NotBlank(message = "Mã sản phẩm không được để trống")
	private String productName;
	@NotNull(message = "Giá thành sản phẩm không được để trống")
	private Double price;
	@NotNull(message = "Số lượng tồn kho không được để trống")
	private Integer stock;
	private String description;
	private String brand;
	private String firstCategoryName;
	private String secondCategoryName;
	private List<String> firstCategories;
	private List<String> secondCategories;
//	@Size(max = 5 * 1024 * 1024, message = "Kích thước tệp không được vượt quá 5MB")
	private FilePart coverImage;
//	@FileSizeList(max = 5 * 1024 * 1024, message = "Kích thước mỗi ảnh không được vượt quá 5MB")
	private List<FilePart> newImages;
//	@Size(max = 10 * 1024 * 1024, message = "Kích thước video không được vượt quá 10MB")
	private FilePart video;
	
	private List<String> removeImages;
	
	private boolean isNew;
}
