const express = require('express');
const router = express.Router();
const invoiceController = require('../controllers/invoiceController');

router.post('/generate', invoiceController.generateInvoicePdf);
router.get('/test',(req,res)=>{
    res.status(200).json({message:"test"})
})

module.exports = router;