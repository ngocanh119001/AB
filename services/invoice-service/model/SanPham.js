class SanPham {
    constructor(maSanPham, tenSanPham, giaBan, soLuongTon, moTa, maDanhMuc, maNguoiBanHang) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.moTa = moTa;
        this.maDanhMuc = maDanhMuc;
        this.maNguoiBanHang = maNguoiBanHang;
    }

    // Phương thức cập nhật số lượng tồn kho
    capNhatSoLuong(soLuongMoi) {
        this.soLuongTon = soLuongMoi;
        return this.soLuongTon;
    }

    // Phương thức tính giá sau khi áp dụng khuyến mãi
    tinhGiaSauKhuyenMai(phanTramGiam) {
        if (phanTramGiam < 0 || phanTramGiam > 100) {
            throw new Error("Phần trăm giảm không hợp lệ!");
        }
        return this.giaBan * (1 - phanTramGiam / 100);
    }
}

module.exports = SanPham;
