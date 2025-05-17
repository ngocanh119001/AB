package iuh.fit.se.serviceImpl;



import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsService implements iuh.fit.se.service.ThirdPartyService {

    @Value("${cloudfront.url}")
    private String cloudFrontUrl = "";
    @Value("${s3.name}")
    private String bucketName = "";

    private final S3AsyncClient s3AsyncClient;

    public Mono<String> uploadFile(FilePart file) {
        String fileName = System.currentTimeMillis() + "_" + file.filename();
        log.info("Upload file: {}", fileName);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.headers().getContentType() != null 
                        ? file.headers().getContentType().toString() 
                        : "application/octet-stream")
                .build();

        return file.content()
        		.doOnNext(dataBuffer -> log.debug("Processing dataBuffer with size: {}", dataBuffer.readableByteCount()))
                .map(dataBuffer -> {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(dataBuffer.readableByteCount());
                    dataBuffer.toByteBuffer(byteBuffer);
                    DataBufferUtils.release(dataBuffer); // Ensure proper resource cleanup
                    return byteBuffer;
                })
                .collectList()
                .doOnNext(byteBuffers -> log.info("Collected {} byte buffers", byteBuffers.size()))
                .flatMap(byteBuffers -> {
                    // Convert List<ByteBuffer> to ByteBuffer[]
                    ByteBuffer[] buffersArray = byteBuffers.toArray(new ByteBuffer[0]);
                    AsyncRequestBody requestBody = AsyncRequestBody.fromByteBuffers(buffersArray);
                    return Mono.fromFuture(s3AsyncClient.putObject(putObjectRequest, requestBody))
                            .thenReturn(cloudFrontUrl + fileName);
                })
                .doOnError(e -> log.error("Error uploading file: {}", file.filename(), e))
                .onErrorMap(e -> new RuntimeException("Failed to upload file: " + file.filename(), e));
    }

 // Phương thức xóa file với xử lý CloudFront URL
    public Mono<Void> deleteFile(String fileKey) {
        log.info("Deleting file from S3: {}", fileKey);

        // Xóa đường dẫn CloudFront nếu có
        String cleanedFileKey = fileKey;
        if (fileKey != null && !cloudFrontUrl.isEmpty() && fileKey.startsWith(cloudFrontUrl)) {
            cleanedFileKey = fileKey.replace(cloudFrontUrl, "");
        }

        // Tạo DeleteObjectRequest
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(cleanedFileKey)
                .build();

        // Gọi S3AsyncClient để xóa file
        return Mono.fromFuture(s3AsyncClient.deleteObject(deleteObjectRequest))
                .doOnSuccess(response -> log.info("Successfully deleted file: {}", fileKey)) // Giữ nguyên fileKey gốc
                .doOnError(e -> log.error("Error deleting file: {}", fileKey, e)) // Giữ nguyên fileKey gốc
                .onErrorMap(e -> new RuntimeException("Failed to delete file: " + fileKey, e)) // Giữ nguyên fileKey gốc
                .then(); // Trả về Mono<Void>
    }
    
    
}