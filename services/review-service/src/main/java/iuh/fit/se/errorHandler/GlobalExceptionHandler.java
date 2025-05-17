package iuh.fit.se.errorHandler;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create(exchange.getRequest().getURI().getPath()));
        log.error("Internal Server Error: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Code", "SERVER_ERROR")
                .body(problemDetail));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("errors", errors);
        log.warn("Validation Error: {}", errors);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Error-Code", "VALIDATION_FAILED")
                .body(problemDetail));
    }

    @ExceptionHandler(MongoException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleMongoException(MongoException ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Database Error");
        problemDetail.setType(URI.create(exchange.getRequest().getURI().getPath()));
        log.error("MongoDB Error: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Code", "DATABASE_ERROR")
                .body(problemDetail));
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleRuntimeException(RuntimeException ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Runtime Error");
        problemDetail.setType(URI.create(exchange.getRequest().getURI().getPath()));
        
        log.error("RuntimeException Error: {}", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("X-Error-Code", "Runtime Error")
                .body(problemDetail));
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleDuplicateKey(DuplicateKeyException ex, ServerWebExchange exchange) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Duplicate Key Error");
        problemDetail.setType(URI.create(exchange.getRequest().getURI().getPath()));
        
        log.warn("DuplicateKeyException: {}", ex.getMessage());

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Error-Code", "DUPLICATE_KEY")
                .body(problemDetail));
    }
}