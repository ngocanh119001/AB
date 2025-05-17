package org.example.service;

import org.example.model.CccdInfo;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OcrService {

    public BufferedImage preprocessImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);

        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int x = 0; x < grayImage.getWidth(); x++) {
            for (int y = 0; y < grayImage.getHeight(); y++) {
                int pixel = grayImage.getRGB(x, y);
                Color color = new Color(pixel);
                int red = (int) (color.getRed() * 1.2);
                int green = (int) (color.getGreen() * 1.2);
                int blue = (int) (color.getBlue() * 1.2);
                red = Math.min(red, 255);
                green = Math.min(green, 255);
                blue = Math.min(blue, 255);
                grayImage.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }

        return grayImage;
    }

    public String extractTextFromImage(File imageFile) throws IOException {
        BufferedImage processedImage = preprocessImage(imageFile);
        File tempFile = File.createTempFile("processed-", ".jpg");
        ImageIO.write(processedImage, "jpg", tempFile);

        net.sourceforge.tess4j.Tesseract tesseract = new net.sourceforge.tess4j.Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("vie");
        try {
            return tesseract.doOCR(tempFile);
        } catch (net.sourceforge.tess4j.TesseractException e) {
            throw new IOException("Lỗi khi thực hiện OCR.", e);
        }
    }

    public CccdInfo parseCccdInfo(String rawText) {
        CccdInfo info = new CccdInfo();
        String[] lines = rawText.split("\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim().toLowerCase();

            if (line.contains("số định danh") || line.contains("personal identification")) {
                String raw = getNextLineValue(lines, i);
                String digitsOnly = raw.replaceAll("[^0-9]", "");  // chỉ giữ lại chữ số
                info.setSoCccd(digitsOnly);
            } else if (line.contains("họ") && line.contains("tên")) {
                String rawName = getNextLineValue(lines, i);
                String cleanName = extractName(rawName);
                info.setHoTen(cleanName);

            } else if (line.contains("ngày") && line.contains("sinh")) {
                // Trường hợp dòng chứa cả ngày sinh và giới tính
                String dobLine = getNextLineValue(lines, i);
                String[] dobGender = dobLine.split("giới tính|sex", 2);
                // Trích xuất ngày sinh có định dạng dd/MM/yyyy
                String ngaySinh = extractDate(dobGender[0]);
                info.setNgaySinh(ngaySinh);

                if (dobGender.length > 1) {
                    info.setGioiTinh(dobGender[1].trim());
                }
            else if (line.contains("giới tính") || line.contains("sex")) {
                    // Dùng regex để lấy phần sau "sex:" hoặc "giới tính:", không bao gồm phần "sex:" hoặc "giới tính:"
                    Pattern genderPattern = Pattern.compile("(?i)(sex)[\\s:/]*([^\\n]+)");
                    Matcher matcher = genderPattern.matcher(line);
                    if (matcher.find()) {
                        // Lấy phần sau dấu ":" (kể cả khoảng trắng và ký tự đặc biệt) và loại bỏ phần "sex:" hoặc "giới tính:"
                        String gender = matcher.group(2).trim();

                        // Kiểm tra các từ đặc biệt để chuyển thành "Nam"
                        if (gender.matches(".*(blạm|Ảlạmr|h_lamf|am|ạm).*")) {
                            info.setGioiTinh("Nam");
                        } else {
                            // Nếu không có các từ đặc biệt, set giới tính là "Nữ"
                            info.setGioiTinh("Nữ");
                        }
                    } else {
                        // Nếu không tìm thấy, fallback lấy dòng kế tiếp
                        info.setGioiTinh(getNextLineValue(lines, i));
                    }



                }
            }else if (line.contains("quốc tịch") || line.contains("Nationality")) {
                // Dùng regex để lấy phần sau "nationaliy:", không bao gồm phần "nationaliy:"
                Pattern nationalityPattern = Pattern.compile("(?i)(y)[\\s:/]*([^\\n]+)");
                Matcher matcher = nationalityPattern.matcher(line);
                if (matcher.find()) {
                    String raw = matcher.group(2).trim().toLowerCase();

                    // Tìm chỉ số bắt đầu từ 'v' và kết thúc ở 'm'
                    int start = raw.indexOf('v');
                    int end = raw.lastIndexOf('m');

                    String result = "";
                    if (start >= 0 && end >= start) {
                        result = raw.substring(start, end + 1);

                        // Xóa các từ đơn lẻ trong kết quả
                        result = Arrays.stream(result.split("\\s+"))
                                .filter(word -> word.length() > 1)
                                .collect(Collectors.joining(" "));
                    }

                    info.setQuocTich(result);
                } else {
                    info.setQuocTich(""); // fallback
                }
            }


        }

        return info;
    }

    private String extractName(String input) {
        // Loại bỏ các ký tự không phải chữ cái, dấu cách và chữ in hoa tiếng Việt
        input = input.replaceAll("[^A-ZÀÁẠÃẢĂẮẰẶẴẲÂẤẦẬẪẨĐÈÉẸẼẺÊỀẾỆỄỂÌÍỊĨỈÒÓỌÕỎÔỐỒỘỖỔƠỚỜỢỠỞÙÚỤŨỦƯỨỪỰỮỬỲÝỴỸỶ\\s]", "");

        // Bỏ các từ rác ở đầu như "HC", "H:", "HO", "HỌ",...
        input = input.replaceAll("^(H[C|:]*|HO|HỌ)\\s*", "");

        return input.trim();
    }


    private String extractDate(String input) {
        // Regex cho định dạng dd/MM/yyyy (ví dụ: 01/12/1990)
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\b\\d{2}/\\d{2}/\\d{4}\\b").matcher(input);
        if (matcher.find()) {
            return matcher.group();  // Trả về chuỗi khớp đầu tiên
        }
        return "";
    }
    private String getNextLineValue(String[] lines, int index) {
        if (index + 1 < lines.length) {
            return lines[index + 1].trim();
        }
        return "";
    }
}
