const fakeData = require("../data/fakeData");

const getOrderById = (req, res) => {
    const { orderId } = req.params;

    // Tìm hóa đơn theo mã
    const order = fakeData.donHangs.find(dh => dh.maHoaDon === orderId);
    if (!order) {
        return res.status(404).json({ message: "Không tìm thấy hóa đơn" });
    }

    // Lấy thông tin người mua hàng
    const buyer = fakeData.nguoiMuaHangs.find(nmh =>
        nmh.hoaDon.some(hd => hd.maHoaDon === orderId)
    );

    // Lấy danh sách người bán từ sản phẩm trong hóa đơn
    const sellers = new Map(); // Map để lưu thông tin người bán theo mã người bán
    const orderDetails = fakeData.chiTietHoaDons
        .filter(cthd => cthd.maHoaDon === orderId)
        .map(cthd => {
            const product = fakeData.sanPhams.find(sp => sp.maSanPham === cthd.maSanPham);
            if (product) {
                // Thêm người bán vào danh sách nếu chưa có
                if (!sellers.has(product.maNguoiBanHang)) {
                    const seller = fakeData.nguoiBanHangs.find(nb => nb.maNguoiBan === product.maNguoiBanHang);
                    if (seller) {
                        sellers.set(seller.maNguoiBan, {
                            maNguoiBanHang: seller.maNguoiBan,
                            tenShop: seller.tenShop,
                            moTaShop: seller.moTaShop,
                        });
                    }
                }
            }
            return {
                maSanPham: product?.maSanPham,
                tenSanPham: product?.tenSanPham,
                soLuong: cthd.soLuong,
                giaBan: product?.giaBan,
                tongTien: cthd.soLuong * (product?.giaBan || 0),
            };
        });

    // Lấy địa chỉ giao hàng
    const deliveryAddress = buyer?.diaChi?.[0] || {};

    // Kết hợp tất cả thông tin
    const response = {
        maHoaDon: order.maHoaDon,
        ngayTao: order.ngayTao,
        ngayGiaoHangDuKien: order.ngayGiaoHangDuKien,
        ngayGuiHang: order.ngayGuiHang,
        trangThai: order.trangThai,
        diaChiGiaoHang: deliveryAddress,
        nguoiMua: {
            maNguoiMuaHang: buyer?.maNguoiMuaHang,
            ngayDangKy: buyer?.ngayDangKy,
            soLuongDonHang: buyer?.soLuongDonHang,
            diemTichLuy: buyer?.diemTichLuy,
        },
        nguoiBan: Array.from(sellers.values()), // Chuyển Map thành mảng
        chiTietHoaDon: orderDetails,
        tongTien: orderDetails.reduce((sum, item) => sum + item.tongTien, 0),
    };

    return res.status(200).json(response);
};

const getOrdersByBuyerId = (req, res) => {
    const { buyerId } = req.params;

    // Tìm người mua theo ID
    const buyer = fakeData.nguoiMuaHangs.find(nmh => nmh.maNguoiMua === buyerId);
    if (!buyer) {
        return res.status(404).json({ message: "Không tìm thấy người mua" });
    }

    // Lấy danh sách hóa đơn của người mua
    const orders = buyer.hoaDon.map(hd => {
        // Tìm hóa đơn chi tiết trong danh sách đơn hàng
        const order = fakeData.donHangs.find(dh => dh.maHoaDon === hd.maHoaDon);
        if (!order) return null;

        // Lấy danh sách sản phẩm trong hóa đơn
        const sellers = new Map();
        const orderDetails = fakeData.chiTietHoaDons
            .filter(cthd => cthd.maHoaDon === order.maHoaDon)
            .map(cthd => {
                const product = fakeData.sanPhams.find(sp => sp.maSanPham === cthd.maSanPham);
                if (product) {
                    // Thêm người bán nếu chưa có trong danh sách
                    if (!sellers.has(product.maNguoiBanHang)) {
                        const seller = fakeData.nguoiBanHangs.find(nb => nb.maNguoiBan === product.maNguoiBanHang);
                        if (seller) {
                            sellers.set(seller.maNguoiBan, {
                                maNguoiBanHang: seller.maNguoiBan,
                                tenShop: seller.tenShop,
                                moTaShop: seller.moTaShop,
                            });
                        }
                    }
                }
                return {
                    maSanPham: product?.maSanPham,
                    tenSanPham: product?.tenSanPham,
                    soLuong: cthd.soLuong,
                    giaBan: product?.giaBan,
                    tongTien: cthd.soLuong * (product?.giaBan || 0),
                };
            });

        // Lấy địa chỉ giao hàng (nếu có)
        const deliveryAddress = buyer?.diaChi?.[0] || {};

        return {
            maHoaDon: order.maHoaDon,
            ngayTao: order.ngayTao,
            ngayGiaoHangDuKien: order.ngayGiaoHangDuKien,
            ngayGuiHang: order.ngayGuiHang,
            trangThai: order.trangThai,
            diaChiGiaoHang: deliveryAddress,
            nguoiBan: Array.from(sellers.values()), // Chuyển Map thành mảng
            chiTietHoaDon: orderDetails,
            tongTien: orderDetails.reduce((sum, item) => sum + item.tongTien, 0),
        };
    }).filter(order => order !== null); // Loại bỏ các hóa đơn không tìm thấy

    return res.status(200).json(orders);
};

module.exports = { getOrderById, getOrdersByBuyerId };
