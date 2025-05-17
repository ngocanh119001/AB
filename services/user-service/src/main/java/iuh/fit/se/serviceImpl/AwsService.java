package iuh.fit.se.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsService implements iuh.fit.se.service.ThirdPartyService {

    @Value("${cloudfront.url}")
    private String cloudFrontUrl;

    @Value("${s3.name}")
    private String bucketName;

    private final S3Client s3Client; // S3Client blocking

    // ----------- UPLOAD (BLOCKING) -----------
    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        log.info("Uploading file (blocking): {}", fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // Upload dùng InputStream hoặc byte[]
        try {
        	s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        }
        catch (Exception e) {
			log.error("Error uploading file: {}", e.getMessage());
			throw new RuntimeException("Failed to upload file", e);
		}
        
        return cloudFrontUrl + fileName;
    }

    // ----------- DELETE (BLOCKING) -----------
    public void deleteFile(String fileKey) {
        log.info("Deleting file (blocking): {}", fileKey);

        String cleanedFileKey = fileKey.replace(cloudFrontUrl, "");
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(cleanedFileKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}