const express = require('express');
const expressLayouts = require('express-ejs-layouts');
const path = require('path');
const homeRoutes = require('./routes/home-routes');
const orderRouter = require("./routes/orderRouter");
const pdfRoutes = require("./routes/pdfRoutes");
const connectDB = require("./config/db");

const Product = require("./models/Product");

const app = express();

connectDB();

app.use(expressLayouts);
app.set('view engine', 'ejs');
app.use(express.static(path.join(__dirname, 'public')));
app.use('/docs', express.static(path.join(__dirname,'docs')))
// Correct usage of homeRoutes.routes
app.use(homeRoutes.routes);

app.use("/api", orderRouter);
app.use("/api/pdf", pdfRoutes);




const eurekaClient = require("./eurekaClient");

// Route healthcheck để Eureka ping
app.get("/info", (req, res) => res.send("PDF Service is running"));

// Route mới để lấy danh sách sản phẩm
app.get("/products", async (req, res) => {
  try {
    const products = await Product.find({});

    res.render('products', { products }); 
    // --> Render file views/products.ejs và truyền biến "products" xuống giao diện
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Server error' });
  }
});


app.listen(3000, () => {
  console.log('Server is running on port 3000');
  eurekaClient.start();
});
