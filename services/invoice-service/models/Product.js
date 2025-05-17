const mongoose = require("mongoose");

const ProductSchema = new mongoose.Schema({
    name: String,
    description: String,
    unit: String,
    quantity: Number,
    price: Number,
    imgurl: String
});

module.exports = mongoose.model("Product", ProductSchema);
