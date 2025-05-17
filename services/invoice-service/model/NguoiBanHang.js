class NguoiBanHang {
    constructor(maNguoiBan, ngayThamGia, tenShop, moTaShop, vouchers = []) {
        this.maNguoiBan = maNguoiBan; // Thêm mã người bán
        this.ngayThamGia = ngayThamGia;
        this.tenShop = tenShop;
        this.moTaShop = moTaShop;
        this.vouchers = vouchers;
    }

    // Đăng sản phẩm mới
    dangSanPham(sanPham) {
        console.log(`Sản phẩm ${sanPham.tenSanPham} đã được đăng bởi ${this.tenShop}`);
    }

    // Cập nhật thông tin sản phẩm
    capNhatSanPham(sanPham) {
        console.log(`Sản phẩm ${sanPham.tenSanPham} đã được cập nhật.`);
    }

    // Xóa sản phẩm
    xoaSanPham(sanPham) {
        console.log(`Sản phẩm ${sanPham.tenSanPham} đã bị xóa.`);
    }

    // Xem đánh giá của khách hàng
    xemDanhGia() {
        console.log(`Xem đánh giá của cửa hàng ${this.tenShop}`);
    }

    // Thống kê doanh thu
    thongKeDoanhThu(donHangs) {
        let doanhThu = donHangs.reduce((total, don) => total + don.tinhTongHoaDon(), 0);
        console.log(`Doanh thu của ${this.tenShop} là: ${doanhThu}`);
        return doanhThu;
    }
}

module.exports = NguoiBanHang;
