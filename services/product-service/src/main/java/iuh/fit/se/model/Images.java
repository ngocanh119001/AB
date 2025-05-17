package iuh.fit.se.model;

import java.util.List;

import lombok.Data;

@Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Images {
	String coverImage;
	List<String> imageList;
	String video;
	
	public void addImage(String image) {
		if (imageList == null) {
			imageList = new java.util.ArrayList<>();
		}
		imageList.add(image);
	}
	public void removeImage(String image) {
		if (imageList != null) {
			imageList.remove(image);
		}
	}
}
