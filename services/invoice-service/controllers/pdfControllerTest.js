const fs = require("fs");
const pdf = require("pdf-creator-node");
const path = require("path");
const Product = require("../models/Product");

const options = {
    format: "A4",
    orientation: "portrait",
};

const homeview = (req, res) => {
    res.render("home");
};

const taoFile = async (req, res) => {
    try {
        const html = fs.readFileSync(path.join(__dirname, "../views/simple-invoice.html"), "utf-8");
        const products = await Product.find().lean();

        const document = {
            html: html,
            data: {
                products: products.map(p => ({
                    ...p,
                    total: p.quantity * p.price
                })),
                subtotal: products.reduce((sum, p) => sum + (p.quantity * p.price), 0),
                date: new Date().toLocaleDateString("vi-VN")
            },
            path: "", // Không lưu ra file
            type: "buffer" // Trả về buffer
        };

        const pdfBuffer = await pdf.create(document, options);
        
        // Thiết lập headers để tải xuống trực tiếp
        res.setHeader("Content-Type", "application/pdf");
        res.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
        
        res.send(pdfBuffer);

    } catch (error) {
        console.error("Lỗi tạo PDF:", error);
        res.status(500).send("Lỗi trong quá trình tạo hóa đơn");
    }
};

module.exports = { homeview, taoFile };