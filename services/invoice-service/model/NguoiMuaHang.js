class NguoiMuaHang {
    constructor(ten, maNguoiMua, ngayDangKy, soLuongDonHang, diemTichLuy, diaChi = [], hoaDon = []) {
        this.ten = ten;
        this.maNguoiMua = maNguoiMua; // Thêm mã người mua
        this.ngayDangKy = ngayDangKy;
        this.soLuongDonHang = soLuongDonHang;
        this.diemTichLuy = diemTichLuy;
        this.diaChi = diaChi;
        this.hoaDon = hoaDon;
    }

    // Đặt hàng mới
    datHang(donHang) {
        this.hoaDon.push(donHang);
        this.soLuongDonHang += 1;
        console.log(`Người mua đã đặt hàng ${donHang.maHoaDon}`);
        return donHang;
    }

    // Hủy đơn hàng theo mã hóa đơn
    huyDonHang(maHoaDon) {
        let index = this.hoaDon.findIndex(don => don.maHoaDon === maHoaDon);
        if (index !== -1) {
            this.hoaDon[index].huyHoaDon();
            console.log(`Người mua đã hủy đơn hàng ${maHoaDon}`);
        } else {
            console.log(`Không tìm thấy đơn hàng ${maHoaDon}`);
        }
    }

    // Xem lịch sử mua hàng
    xemLichSuMuaHang() {
        return this.hoaDon.map(don => don.xuatHoaDon());
    }
}

module.exports = NguoiMuaHang;
