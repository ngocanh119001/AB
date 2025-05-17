const express = require('express');
const mongoose = require('mongoose');
const customerRoutes = require('./routers/customer');

const app = express();

// Kết nối MongoDB
mongoose.connect('mongodb://127.0.0.1:27017/userServiceDB', {
  useNewUrlParser: true,
  useUnifiedTopology: true
})
.then(() => console.log('✅ Kết nối MongoDB thành công!'))
.catch((err) => console.error('❌ Lỗi kết nối MongoDB:', err));

// Middleware để xử lý dữ liệu JSON từ request
app.use(express.json());

// Sử dụng các routes cho customer
app.use('/api/users/customer', customerRoutes);

// Chạy server tại cổng 3000
app.listen(3000, () => {
  console.log('🚀 Server đang chạy tại http://localhost:3000');
});
