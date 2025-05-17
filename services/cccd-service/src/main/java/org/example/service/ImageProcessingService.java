package org.example.service;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class ImageProcessingService {

    // Làm sắc nét ảnh
    public BufferedImage sharpenImage(BufferedImage image) {
        float[] sharpenKernel = {
                -1f, -1f, -1f,
                -1f,  9f, -1f,
                -1f, -1f, -1f
        };
        BufferedImageOp sharpenOp = new ConvolveOp(new Kernel(3, 3, sharpenKernel));
        return sharpenOp.filter(image, null);
    }

    // Lọc nhiễu (Noise Reduction)
    public BufferedImage reduceNoise(BufferedImage image) {
        // Dùng thuật toán làm mịn ảnh để giảm nhiễu (Gaussian Blur)
        float[] blurKernel = {
                1f / 9f, 1f / 9f, 1f / 9f,
                1f / 9f, 1f / 9f, 1f / 9f,
                1f / 9f, 1f / 9f, 1f / 9f
        };
        BufferedImageOp blurOp = new ConvolveOp(new Kernel(3, 3, blurKernel));
        return blurOp.filter(image, null);
    }

    // Chuyển ảnh thành ảnh đen trắng (Grayscale)
    public BufferedImage convertToGrayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }

    // Cân bằng độ sáng (tăng độ sáng)
    public BufferedImage enhanceBrightness(BufferedImage image) {
        BufferedImage enhancedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y);
                Color color = new Color(pixel);

                // Tăng độ sáng (có thể thử các giá trị khác nhau)
                int red = Math.min((int) (color.getRed() * 1.5), 255);
                int green = Math.min((int) (color.getGreen() * 1.5), 255);
                int blue = Math.min((int) (color.getBlue() * 1.5), 255);

                enhancedImage.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return enhancedImage;
    }

    // Điều chỉnh độ tương phản ảnh
    public BufferedImage adjustContrast(BufferedImage image, float contrast) {
        RescaleOp rescaleOp = new RescaleOp(contrast, 0, null);
        return rescaleOp.filter(image, null);
    }

    // Các phương pháp tiền xử lý ảnh (mỗi bước có thể tùy chọn)
    public BufferedImage preprocessImage(BufferedImage image) {
        image = convertToGrayscale(image);
        image = enhanceBrightness(image);  // Tăng sáng ảnh
        image = reduceNoise(image);  // Lọc nhiễu ảnh
        image = sharpenImage(image);  // Làm sắc nét ảnh
        return image;
    }

    // Lưu ảnh đã tiền xử lý vào file
    public File saveProcessedImage(BufferedImage image) throws IOException {
        File tempFile = File.createTempFile("processed-", ".jpg");
        ImageIO.write(image, "jpg", tempFile);
        return tempFile;
    }
}
