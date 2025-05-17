const fs = require("fs");
const pdf = require("pdf-creator-node");
const path = require("path");
const options = require("../helpers/option");
const Product = require("../models/Product");

const homeview = async (req, res, next) => {
    res.render("home");
};

const taoFile = async (req, res, next) => {
    try {
        const html = fs.readFileSync(path.join(__dirname, "../views/template.html"), "utf-8");
        const filename = Math.random() + "_doc" + ".pdf";

        // Lấy dữ liệu từ MongoDB
        const data = await Product.find();
        let array = [];

        data.forEach((d) => {
            const prod = {
                name: d.name,
                description: d.description,
                unit: d.unit,
                quantity: d.quantity,
                price: d.price,
                total: d.quantity * d.price,
                imgurl: d.imgurl
            };
            array.push(prod);
        });

        let subtotal = array.reduce((sum, item) => sum + item.total, 0);
        const tax = (subtotal * 20) / 100;
        const grandtotal = subtotal - tax;

        const obj = {
            prodlist: array,
            subtotal,
            tax,
            gtotal: grandtotal
        };

        const document = {
            html: html,
            data: {
                products: obj
            },
            path: "./docs/" + filename
        };

        await pdf.create(document, options);

        const filepath = "http://localhost:3000/docs/" + filename;

        res.render("download", {
            path: filepath
        });
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: "Lỗi tạo PDF" });
    }
};

module.exports = {
    homeview,
    taoFile
};
