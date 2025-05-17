const fs = require("fs");
const pdf = require("pdf-creator-node");
const path = require("path");
const options = require("../helpers/options");
const fakeData = require("../data/fakeData");

const generateInvoice = async (req, res) => {
    const { orderId } = req.params;

    // Tìm hóa đơn theo mã
    const order = fakeData.donHangs.find(dh => dh.maHoaDon === orderId);
    if (!order) {
        return res.status(404).json({ message: "Không tìm thấy hóa đơn" });
    }

    // Tìm người mua theo danh sách người mua
    const buyer = fakeData.nguoiMuaHangs.find(nmh =>
        nmh.hoaDon.some(hd => hd.maHoaDon === orderId)
    );

    console.log("check nguoi mua: ", buyer)

    if (!buyer) {
        return res.status(404).json({ message: "Không tìm thấy người mua cho hóa đơn này" });
    }

    // Lấy danh sách người bán từ sản phẩm trong hóa đơn
    const sellers = new Map();
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
                tenSanPham: product?.tenSanPham || "Không xác định",
                soLuong: cthd.soLuong,
                giaBan: product?.giaBan || 0,
                tongTien: cthd.soLuong * (product?.giaBan || 0),
            };
        });

    // Lấy địa chỉ giao hàng (nếu có)
    const deliveryAddress = buyer.diaChi.length > 0 ? buyer.diaChi[0] : { diaChi: "Không có địa chỉ" };

    console.log("check dia chi: ", deliveryAddress)

    // Tính tổng tiền
    const totalAmount = orderDetails.reduce((sum, item) => sum + item.tongTien, 0);

    // Dữ liệu để đổ vào template
    const documentData = {
        maHoaDon: order.maHoaDon,
        ngayTao: order.ngayTao,
        nguoiMua: {
            ten: buyer.ten || "Không xác định",
            maNguoiMuaHang: buyer.maNguoiMua,
            ngayDangKy: buyer.ngayDangKy,
            soLuongDonHang: buyer.soLuongDonHang,
            diemTichLuy: buyer.diemTichLuy,
        },
        diaChiGiaoHang: deliveryAddress,
        nguoiBan: Array.from(sellers.values()), // Chuyển Map thành mảng
        chiTietHoaDon: orderDetails,
        tongTien: totalAmount,
    };

    // Đọc file template
    const html = fs.readFileSync(path.join(__dirname, "../views/invoiceTemplate.html"), "utf-8");

    // Định nghĩa đường dẫn file PDF
    const filename = `invoice_${orderId}.pdf`;
    const document = {
        html: html,
        data: documentData,
        path: `./docs/${filename}`,
    };

    // Tạo PDF
    try {
        await pdf.create(document, options);
        const filePath = `http://localhost:3000/docs/${filename}`;
        return res.status(200).json({ message: "PDF đã tạo", path: filePath });
    } catch (error) {
        console.error(error);
        return res.status(500).json({ message: "Lỗi khi tạo PDF" });
    }
};

module.exports = { generateInvoice };
