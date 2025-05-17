package iuh.fit.se.service;

import org.springframework.web.multipart.MultipartFile;


public interface ThirdPartyService {
	String uploadFile(MultipartFile file);
	void deleteFile(String fileKey);
}