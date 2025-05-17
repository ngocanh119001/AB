const express = require('express');
const router = express.Router();
const customerController = require('../controllers/customerController');

// Lấy danh sách tất cả customer
router.get('/', customerController.getAllCustomers);

// Lấy 1 customer theo ID
router.get('/:id', customerController.getCustomerById);

// Thêm customer mới
router.post('/', customerController.addCustomer);

// Cập nhật customer theo ID
router.put('/:id', customerController.updateCustomer);

// Xóa customer theo ID
router.delete('/:id', customerController.deleteCustomer);

module.exports = router;
