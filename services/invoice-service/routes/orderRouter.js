const express = require("express");
const router = express.Router();
const orderController = require("../controllers/orderController");

// API lấy thông tin hóa đơn theo mã hóa đơn
router.get("/orders/:orderId", orderController.getOrderById);
router.get("/orders/get-orders-by-buyerId/:buyerId", orderController.getOrdersByBuyerId);

module.exports = router;
