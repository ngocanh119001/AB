const mongoose = require('mongoose');

// Địa chỉ (Address)
const AddressSchema = new mongoose.Schema({
  addressId: String,
  recipientName: String,
  recipientPhone: String,
  recipientAddress: String,
  addressType: Number
}, { _id: false });

// Thông tin người dùng (User)
const UserSchema = new mongoose.Schema({
  userId: String,
  tenNguoiDung: String,
  email: String,
  soDienThoai: String,
  roles: [String],
  createdAt: Date,
  updatedAt: Date
}, { _id: false });

// Chi tiết giỏ hàng (CartDetail)
const CartDetailSchema = new mongoose.Schema({
  productId: String,
  quantity: Number,
  customerId: String,
  firstCategory: String,
  secondCategory: String
}, { _id: false });

// Tài khoản ngân hàng (BankAccount)
const BankAccountSchema = new mongoose.Schema({
  bankNumber: String,
  dueDate: Date,
  ownerName: String,
  address: String,
  zipCode: Number
}, { _id: false });

// Customer chính
const CustomerSchema = new mongoose.Schema({
  customerId: String,
  soLuongDonHang: Number,
  diemTichLuy: Number,
  address: [AddressSchema],
  user: UserSchema,
  cart: CartDetailSchema,
  bank: BankAccountSchema
});

module.exports = mongoose.model('Customer', CustomerSchema);
