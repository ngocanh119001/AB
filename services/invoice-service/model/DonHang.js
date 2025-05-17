class DonHang {
    constructor(maHoaDon, ngayTao, maNguoiBanHang, maNguoiMuaHang, ngayGiaoHangDuKien, ngayGuiHang, diaChi, trangThai) {
        this.maHoaDon = maHoaDon;
        this.ngayTao = ngayTao;
        this.maNguoiBanHang = maNguoiBanHang;
        this.maNguoiMuaHang = maNguoiMuaHang;
        this.ngayGiaoHangDuKien = ngayGiaoHangDuKien;
        this.ngayGuiHang = ngayGuiHang;
        this.diaChi = diaChi;
        this.trangThai = trangThai;
        this.chiTietHoaDons = []; // Danh sách chi tiết hóa đơn
    }

    // Thêm chi tiết hóa đơn
    themChiTietHoaDon(chiTietHoaDon) {
        this.chiTietHoaDons.push(chiTietHoaDon);
        console.log(`Thêm chi tiết hóa đơn cho đơn hàng ${this.maHoaDon}`);
    }

    // Tính tổng tiền của đơn hàng
    tinhTongHoaDon() {
        return this.chiTietHoaDons.reduce((total, chiTiet) => total + chiTiet.tinhTongTien(), 0);
    }

    // Hủy đơn hàng
    huyHoaDon() {
        this.trangThai = "Đã hủy";
        console.log(`Đơn hàng ${this.maHoaDon} đã bị hủy.`);
    }

    // Xuất thông tin đơn hàng
    xuatHoaDon() {
        return `Đơn hàng: ${this.maHoaDon}, Ngày tạo: ${this.ngayTao}, Trạng thái: ${this.trangThai}`;
    }
}

module.exports = DonHang;
