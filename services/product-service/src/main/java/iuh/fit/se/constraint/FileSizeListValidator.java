package iuh.fit.se.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;

public class FileSizeListValidator implements ConstraintValidator<FileSizeList, List<FilePart>> {
    private long maxSize;

    @Override
    public void initialize(FileSizeList constraintAnnotation) {
        this.maxSize = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<FilePart> fileParts, ConstraintValidatorContext context) {
        if (fileParts == null || fileParts.isEmpty()) {
            return true; // Nếu danh sách rỗng hoặc null, bỏ qua kiểm tra
        }

        for (FilePart filePart : fileParts) {
            // Lấy kích thước file bất đồng bộ
            Mono<Long> fileSizeMono = filePart.content()
                    .map(dataBuffer -> (long) dataBuffer.readableByteCount())
                    .reduce(0L, Long::sum);

            // Chuyển Mono thành blocking để kiểm tra
            Long fileSize = fileSizeMono.block();
            if (fileSize != null && fileSize > maxSize) {
                return false; // File vượt quá kích thước cho phép
            }
        }
        return true;
    }
}