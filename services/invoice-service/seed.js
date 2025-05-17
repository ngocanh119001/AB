const mongoose = require("mongoose");
const Product = require("./models/Product");

// Sửa chỗ này: dùng 'mongodb' thay vì 'localhost', cổng 27017
const MONGODB_URI = "mongodb://admin:password@mongodb:27017/mydb?authSource=admin";

async function seedDatabase() {
  try {
    await mongoose.connect(MONGODB_URI, {
      useNewUrlParser: true,
      useUnifiedTopology: true
    });
    console.log("✅ Đã kết nối thành công tới MongoDB");

    await Product.deleteMany({});
    console.log("🧹 Đã dọn sạch dữ liệu cũ");

    const sampleProducts = [
      {
        name: "iPhone 15 Pro",
        description: "Điện thoại flagship của Apple",
        unit: "cái",
        quantity: 10,
        price: 25000000,
        imgurl: "https://example.com/iphone.jpg"
      },
      {
        name: "MacBook Pro M2",
        description: "Laptop cao cấp 16-inch",
        unit: "cái",
        quantity: 5,
        price: 45000000,
        imgurl: "https://example.com/macbook.jpg"
      },
      {
        name: "AirPods Pro 2",
        description: "Tai nghe không dây chống ồn",
        unit: "cái",
        quantity: 20,
        price: 6000000,
        imgurl: "https://example.com/airpods.jpg"
      }
    ];

    const createdProducts = await Product.insertMany(sampleProducts);
    console.log(`🌱 Đã thêm thành công ${createdProducts.length} sản phẩm`);

  } catch (error) {
    console.error("❌ Lỗi trong quá trình seed dữ liệu:", error);
  } finally {
    await mongoose.disconnect();
    console.log("🔌 Đã ngắt kết nối MongoDB");
  }
}

seedDatabase();
