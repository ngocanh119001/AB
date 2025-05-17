class DiaChi {
    constructor(hoTenNguoiNhan, diaChiCuThe, tinh, quan, phuong, loaiDiaChi) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
        this.diaChiCuThe = diaChiCuThe;
        this.tinh = tinh;
        this.quan = quan;
        this.phuong = phuong;
        this.loaiDiaChi = loaiDiaChi; // 0: Bình thường, 1: Địa chỉ nhận hàng mặc định, 2: Địa chỉ giao hàng
    }

    // Lấy thông tin đầy đủ của địa chỉ
    getThongTinDiaChi() {
        return `${this.hoTenNguoiNhan}, ${this.diaChiCuThe}, ${this.phuong}, ${this.quan}, ${this.tinh}`;
    }

    // Kiểm tra địa chỉ có phải mặc định không
    laDiaChiMacDinh() {
        return this.loaiDiaChi === 1;
    }
}

module.exports = DiaChi;
