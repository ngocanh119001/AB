package org.example.controller;

import java.io.File;
import java.io.IOException;

import org.example.model.CccdInfo;
import org.example.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
        System.out.println("Bắt đầu xử lý yêu cầu upload.");

        try {
            if (file == null || file.isEmpty()) {
                System.out.println("Lỗi: File không được để trống.");
                return ResponseEntity.badRequest().body("File không được để trống.");
            }

            File tempFile = File.createTempFile("upload-", ".jpg");
            file.transferTo(tempFile);
            System.out.println("File đã được lưu tại: " + tempFile.getAbsolutePath());

            String ocrText = ocrService.extractTextFromImage(tempFile);
            System.out.println("Text OCR đã trích xuất: " + ocrText);

            if (ocrText == null || ocrText.isEmpty()) {
                System.out.println("Lỗi: Không có văn bản nào được trích xuất từ ảnh.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không có văn bản nào được trích xuất từ ảnh.");
            }

            CccdInfo info = ocrService.parseCccdInfo(ocrText);
            System.out.println("Thông tin CCCD đã phân tích: " + info);

//            if (info == null || info.getSoCccd() == null || info.getSoCccd().equals("-")) {
//                System.out.println("Lỗi: Thông tin CCCD không hợp lệ.");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thông tin CCCD không hợp lệ.");
//            }

//            CccdInfo savedInfo = cccdRepository.save(info);
//            System.out.println("Thông tin đã lưu thành công vào MongoDB: " + savedInfo);

            return ResponseEntity.ok(info);

        } catch (IOException e) {
            System.out.println("Lỗi khi xử lý file: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý file.");
        } catch (Exception e) {
            System.out.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi không xác định.");
        }
    }
}
