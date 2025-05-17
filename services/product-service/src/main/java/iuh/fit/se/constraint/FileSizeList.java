package iuh.fit.se.constraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileSizeListValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSizeList {
    String message() default "Kích thước mỗi tệp không được vượt quá {max} bytes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long max();
}
