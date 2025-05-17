const express = require("express");
const router = express.Router();
const { generateInvoice } = require("../controllers/pdfController");

// API xuất PDF hóa đơn theo mã hóa đơn
router.get("/export-pdf/:orderId", generateInvoice);

module.exports = router;
