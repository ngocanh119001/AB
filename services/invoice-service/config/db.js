const mongoose = require('mongoose');

const connectDB = async () => {
  try {
    // Đổi port kết nối từ 27017 -> 27018
    // Sử dụng tên dịch vụ 'mongodb' thay cho 'localhost' ==> kết nối từ container này đến container mongodb trong docker-compose
    // await mongoose.connect('mongodb://admin:password@mongodb:27018/mydb?authSource=admin', {

    // kết nối nội bộ trong các container nên cổng là 27017
    await mongoose.connect('mongodb://admin:password@mongodb:27017/mydb?authSource=admin');
    console.log('MongoDB Connected...');
  } catch (err) {
    console.error(err.message);
    process.exit(1);
  }
};

module.exports = connectDB;