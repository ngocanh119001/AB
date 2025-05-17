const express = require('express');
// const {homeview,taoFile} = require('../controllers/homeControllers')
const {homeview, taoFile} = require('../controllers/pdfControllerTest')
const router = express.Router()
router.get('/', homeview);
router.get('/download',taoFile);
module.exports={
    routes : router
}