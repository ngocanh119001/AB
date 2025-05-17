package iuh.fit.se.service;

import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Mono;

public interface ThirdPartyService {
	Mono<String> uploadFile(FilePart file);
	Mono<Void> deleteFile(String fileKey); // Thêm phương thức xóa file
	
}