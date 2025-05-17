const express = require("express");
const multer = require("multer");
const Tesseract = require("tesseract.js");
const cors = require("cors");

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

// Hàm chuẩn hóa dữ liệu OCR
function cleanText(text) {
    text = text.replace(/\b(TT|ay|SF|=~|TT|~~|=)\b/gi, "");
    text = text.replace(/\b(ề |ca)\b/gi, "");
    text = text.replace(/\s?ề\s?/gi, "");
    text = text.replace(/\n/g, " "); // Thay dấu xuống dòng thành khoảng trắng
    text = text.replace(/[^\wÀ-ỹ\s\/,]/g, ""); // Chỉ giữ chữ, số, dấu "/", ",", "-"
    text = text.replace(/\s{2,}/g, " ").trim(); // Loại bỏ khoảng trắng thừa

        // Loại bỏ các đoạn văn bản thừa không có ý nghĩa
    
    return text;
}

// Hàm trích xuất thông tin CCCD từ OCR
function extractCCCDInfo(text) {
    text = cleanText(text);

    const result = { hoTen: "", ngaySinh: "", gioiTinh: "", queQuan: "", diaChiThuongTru: "" };

    const nameRegex = /Họ và tên\s*\/\s*Full name\s*:? (.+?) Ngày sinh/i;
    const dobRegex = /Ngày sinh\s*\/\s*Date of birth\s*:? (\d{2}\/\d{2}\/\d{4})/i;
    
    // Regex bắt nhiều trường hợp khác nhau của "Giới tính" và "Quốc tịch"
    const genderRegex = /(Gigi tinh|Giới tính)\s*\/\s*Sex\s*:? (Nam|Nữ)/i;

    
    const hometownRegex = /Quê quán\s*\/\s*Place of origin\s*:? (.+?) Nơi thường trú/i;
    const addressRegex = /Nơi thường trú\s*\/\s*Place of residence\s*:? (.+)/i;

    result.hoTen = (text.match(nameRegex) || [])[1]?.trim() || "";
    result.ngaySinh = (text.match(dobRegex) || [])[1]?.trim() || "";
    result.gioiTinh = (text.match(genderRegex) || [])[2]?.trim() || ""; // [2] do regex có 2 nhóm
    result.queQuan = (text.match(hometownRegex) || [])[1]?.trim() || "";
    result.diaChiThuongTru = (text.match(addressRegex) || [])[1]?.trim() || "";

    return result;
}




// API xử lý upload ảnh CCCD
app.post("/upload", upload.single("image"), async (req, res) => {
    try {
        if (!req.file) {
            return res.status(400).json({ error: "Vui lòng tải lên một ảnh CCCD." });
        }

        // Nhận diện chữ từ ảnh bằng OCR
        const { data: { text } } = await Tesseract.recognize(req.file.buffer, "eng+vie");

        console.log("OCR Output (Trước khi làm sạch):\n", text);

        // Trích xuất thông tin
        const extractedData = extractCCCDInfo(text);

        return res.json({ success: true, data: extractedData });

    } catch (error) {
        return res.status(500).json({ error: "Có lỗi xảy ra", details: error.message });
    }
});

// Khởi chạy server
app.listen(port, () => {
    console.log(`Server đang chạy tại http://localhost:${port}`);
});
