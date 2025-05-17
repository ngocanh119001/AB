package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CccdOcrApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CccdOcrApiApplication.class, args);
        System.out.println("Service đang chạy tại http://localhost:8080/api/ocr/upload");

    }
}
