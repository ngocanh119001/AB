const NguoiBanHang = require("../model/NguoiBanHang.js");
const NguoiMuaHang = require("../model/NguoiMuaHang.js");
const DonHang = require("../model/DonHang.js");
const ChiTietHoaDon = require("../model/ChiTietHoaDon.js");
const SanPham = require("../model/SanPham.js");
const DiaChi = require("../model/DiaChi.js");

// Fake dữ liệu người bán hàng
const nguoiBanHangs = [
    new NguoiBanHang("NB001", "2023-01-10", "Shop ABC", "Chuyên đồ điện tử", ["VOUCHER10"]),
    new NguoiBanHang("NB002", "2022-05-20", "Shop XYZ", "Thời trang nam nữ", ["VOUCHER20"]),
    new NguoiBanHang("NB003", "2021-11-15", "Shop 123", "Phụ kiện điện thoại", ["VOUCHER30"]),
    new NguoiBanHang("NB004", "2020-07-08", "Shop TMDT", "Sản phẩm tiện ích", ["VOUCHER50"]),
];


// Fake dữ liệu sản phẩm
const sanPhams = [
    new SanPham("SP001", "Laptop Dell", 20000000, 10, "Laptop Dell chính hãng", "DM001", "NB001"),
    new SanPham("SP002", "Áo thun nam", 250000, 50, "Áo thun cotton 100%", "DM002", "NB002"),
    new SanPham("SP003", "Tai nghe Bluetooth", 500000, 30, "Tai nghe không dây", "DM003", "NB003"),
    new SanPham("SP004", "Bàn phím cơ", 1500000, 20, "Bàn phím LED RGB", "DM004", "NB004"),
    new SanPham("SP005", "Chuột gaming", 700000, 25, "Chuột có DPI điều chỉnh", "DM005", "NB001"),
];

// Fake dữ liệu địa chỉ
const diaChis = [
    new DiaChi("Nguyễn Văn A", "123 Đường ABC", "Hà Nội", "Cầu Giấy", "Dịch Vọng", 1),
    new DiaChi("Trần Thị B", "456 Đường XYZ", "TP.HCM", "Quận 1", "Bến Thành", 0),
    new DiaChi("Lê Văn C", "789 Đường DEF", "Đà Nẵng", "Hải Châu", "Thanh Bình", 2),
    new DiaChi("Phạm Thị D", "321 Đường GHI", "Hải Phòng", "Ngô Quyền", "Lạch Tray", 1),
];

// Fake dữ liệu người mua hàng
const nguoiMuaHangs = [
    new NguoiMuaHang("Nguyễn Văn A", "NMH001", "2024-02-15", 2, 100, [diaChis[0]], []),
    new NguoiMuaHang("Trần Thị B", "NMH002", "2023-12-10", 5, 200, [diaChis[1]], []),
    new NguoiMuaHang("Lê Văn C", "NMH003", "2023-06-05", 3, 150, [diaChis[2]], []),
    new NguoiMuaHang("Phạm Thị D", "NMH004", "2022-09-20", 4, 180, [diaChis[3]], []),
];

// Fake dữ liệu đơn hàng
const donHangs = [
    new DonHang("DH001", "2024-03-10", "NB001", "NMH001", "2024-03-15", "2024-03-12", "Hà Nội", "Đang xử lý"),
    new DonHang("DH002", "2024-03-08", "NB002", "NMH002", "2024-03-14", "2024-03-10", "TP.HCM", "Đã giao"),
    new DonHang("DH003", "2024-03-05", "NB003", "NMH003", "2024-03-12", "2024-03-07", "Đà Nẵng", "Chờ xác nhận"),
    new DonHang("DH004", "2024-02-28", "NB004", "NMH004", "2024-03-05", "2024-03-02", "Hải Phòng", "Đã hủy"),
];

// Fake dữ liệu chi tiết hóa đơn
const chiTietHoaDons = [
    new ChiTietHoaDon("CTHD001", 2, "DH001", "SP001", "DH001", "VOUCHER10"),
    new ChiTietHoaDon("CTHD002", 1, "DH002", "SP002", "DH002", "VOUCHER20"),
    new ChiTietHoaDon("CTHD003", 3, "DH003", "SP003", "DH003", "VOUCHER30"),
    new ChiTietHoaDon("CTHD004", 1, "DH004", "SP004", "DH004", "VOUCHER50"),
    new ChiTietHoaDon("CTHD005", 2, "DH001", "SP005", "DH001", "VOUCHER50"),
];

// Thêm chi tiết hóa đơn vào đơn hàng
donHangs[0].themChiTietHoaDon(chiTietHoaDons[0]);
donHangs[0].themChiTietHoaDon(chiTietHoaDons[4]);
donHangs[1].themChiTietHoaDon(chiTietHoaDons[1]);
donHangs[2].themChiTietHoaDon(chiTietHoaDons[2]);
donHangs[3].themChiTietHoaDon(chiTietHoaDons[3]);

// Thêm đơn hàng vào người mua hàng
nguoiMuaHangs[0].hoaDon.push(donHangs[0]);
nguoiMuaHangs[1].hoaDon.push(donHangs[1]);
nguoiMuaHangs[2].hoaDon.push(donHangs[2]);
nguoiMuaHangs[3].hoaDon.push(donHangs[3]);

// Xuất dữ liệu fake
console.log("Người bán hàng:", JSON.stringify(nguoiBanHangs, null, 2));
console.log("Sản phẩm:", JSON.stringify(sanPhams, null, 2));
console.log("Địa chỉ:", JSON.stringify(diaChis, null, 2));
console.log("Người mua hàng:", JSON.stringify(nguoiMuaHangs, null, 2));
console.log("Đơn hàng:", JSON.stringify(donHangs, null, 2));
console.log("Chi tiết hóa đơn:", JSON.stringify(chiTietHoaDons, null, 2));

module.exports = {
    nguoiBanHangs,
    sanPhams,
    diaChis,
    nguoiMuaHangs,
    donHangs,
    chiTietHoaDons
};

