class ChiTietHoaDon {
    constructor(maChiTietHoaDon, soLuong, maHoaDon, maSanPham, voucher = null) {
        this.maChiTietHoaDon = maChiTietHoaDon;
        this.soLuong = soLuong;
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.voucher = voucher;
    }

    // Phương thức chuyển đổi đối tượng thành chuỗi
    toString() {
        return `ChiTietHoaDon: { maChiTietHoaDon: ${this.maChiTietHoaDon}, soLuong: ${this.soLuong}, maHoaDon: ${this.maHoaDon}, maSanPham: ${this.maSanPham}, voucher: ${this.voucher} }`;
    }

    // Phương thức tính tổng tiền (giả sử có giá sản phẩm)
    tinhTongTien(giaSanPham) {
        return this.soLuong * giaSanPham;
    }

    // Phương thức cập nhật số lượng
    capNhatSoLuong(soLuongMoi) {
        this.soLuong = soLuongMoi;
    }
}

module.exports = ChiTietHoaDon;
