const PdfPrinter = require('pdfmake');
const path = require('path');

const fontsDirectory = './fonts/';


const printer = new PdfPrinter({
    Roboto: {
        normal: path.join(fontsDirectory, 'Roboto-Regular.ttf'),
        bold: path.join(fontsDirectory, 'Roboto-Medium.ttf'),
        italics: path.join(fontsDirectory, 'Roboto-Italic.ttf'),
        bolditalics: path.join(fontsDirectory, 'Roboto-MediumItalic.ttf')
    }
});

function formatCurrency(amount, currency = 'VND') {
    if (typeof amount !== 'number' || isNaN(amount)) {
        return 'N/A';
    }
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: currency }).format(amount);
}

// Hàm tiện ích định dạng ngày
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    try {
        let date;
        
        // Xử lý định dạng dd-MM-yyyy
        if (/^\d{2}-\d{2}-\d{4}$/.test(dateString)) {
            const parts = dateString.split('-');
            date = new Date(parseInt(parts[2], 10), parseInt(parts[1], 10) - 1, parseInt(parts[0], 10));
        } 
        // Xử lý định dạng yyyy-MM-dd hoặc ISO string
        else if (/^\d{4}-\d{2}-\d{2}/.test(dateString)) {
            date = new Date(dateString);
        }
        // Các định dạng khác
        else {
            date = new Date(dateString);
        }

        if (isNaN(date.getTime())) {
            return dateString;
        }

        return date.toLocaleDateString('vi-VN', {
            day: '2-digit', 
            month: '2-digit', 
            year: 'numeric'
            // Bỏ timeZone: 'UTC' trừ khi bạn chắc chắn cần
        });
    } catch (e) {
        console.error("Error formatting date:", dateString, e);
        return dateString;
    }
}


exports.generateInvoicePdf = async (req, res) => {
    try {
        const orderData = req.body;

        if (!orderData || !orderData.orderId) {
            return res.status(400).json({ message: "Dữ liệu đơn hàng không hợp lệ hoặc thiếu orderId." });
        }

        // Chuẩn bị nội dung chi tiết đơn hàng cho bảng
        const orderDetailsTableBody = [
            [{ text: 'Sản phẩm', style: 'tableHeader' }, { text: 'Số lượng', style: 'tableHeader', alignment: 'right' }, { text: 'Đơn giá', style: 'tableHeader', alignment: 'right' }, { text: 'Thành tiền', style: 'tableHeader', alignment: 'right' }]
        ];

        if (orderData.orderDetails && Array.isArray(orderData.orderDetails)) {
            orderData.orderDetails.forEach(item => {
                const quantity = (item.quantity !== undefined && item.quantity !== null) ? Number(item.quantity) : 0;
                const price = (item.price !== undefined && item.price !== null) ? Number(item.price) : 0;
                orderDetailsTableBody.push([
                    item.productName || 'N/A',
                    { text: quantity.toString(), alignment: 'right' },
                    { text: formatCurrency(price), alignment: 'right' },
                    { text: formatCurrency(quantity * price), alignment: 'right' }
                ]);
            });
        } else {
            orderDetailsTableBody.push([{text: 'Không có chi tiết sản phẩm', colSpan: 4, alignment: 'center', style: 'italicText'}, {}, {}, {}]);
        }


        const documentDefinition = {
            content: [
                { text: 'HÓA ĐƠN BÁN HÀNG', style: 'header', alignment: 'center' },
                { text: `Mã đơn hàng: ${orderData.orderId || 'N/A'}`, style: 'subheader' },
                { text: `Ngày tạo: ${formatDate(orderData.createAt)}`, style: 'subheader', margin: [0, 0, 0, 20] }, // top, right, bottom, left

                {
                    columns: [
                        {
                            width: '*',
                            text: [
                                { text: 'Thông tin người nhận:\n', style: 'subheader2' },
                                `${orderData.deliveryAddress?.recipientName || 'N/A'}\n`,
                                `ĐT: ${orderData.deliveryAddress?.recipientPhone || 'N/A'}\n`,
                                `Địa chỉ: ${orderData.deliveryAddress?.recipientAddress || 'N/A'}`
                            ]
                        },
                        {
                            width: '*',
                            text: [
                                { text: 'Thông tin cửa hàng (Vendor):\n', style: 'subheader2' },
                                `Mã cửa hàng: ${orderData.vendorId || 'N/A'}\n`,
                                // Thêm các thông tin khác của vendor nếu có
                            ],
                            alignment: 'right'
                        }
                    ],
                    columnGap: 10,
                    margin: [0, 0, 0, 15] // bottom margin
                },

                { text: 'Chi tiết đơn hàng:', style: 'subheader2', margin: [0, 10, 0, 5] }, // top, right, bottom, left
                {
                    table: {
                        headerRows: 1,
                        widths: ['*', 'auto', 'auto', 'auto'],
                        body: orderDetailsTableBody
                    },
                    layout: 'lightHorizontalLines'
                },

                // Tổng tiền và voucher
                {
                    table: {
                        widths: ['*', 'auto'],
                        body: [
                            [{}, {text: `Tổng cộng: ${formatCurrency(orderData.totalPrice)}`, style: 'total', alignment: 'right' }],
                            orderData.voucherId ? [{}, { text: `Voucher: ${orderData.voucherId}`, alignment: 'right', style: 'italicText' }] : [{},{}]
                        ]
                    },
                    layout: 'noBorders', // Không có viền cho bảng tổng tiền
                    margin: [0, 10, 0, 5] // top, right, bottom, left
                },

                orderData.notes ? { text: `Ghi chú: ${orderData.notes}`, margin: [0, 10, 0, 10], style: 'italicText' } : {},

                { text: 'Cảm ơn quý khách!', style: 'italicText', alignment: 'center', margin: [0, 30, 0, 0] }
            ],
            defaultStyle: {
                font: 'Roboto'
            },
            styles: {
                header: {
                    fontSize: 18,
                    bold: true,
                    margin: [0, 0, 0, 10]
                },
                subheader: {
                    fontSize: 14,
                    bold: true,
                    margin: [0, 5, 0, 5]
                },
                subheader2: {
                    fontSize: 12,
                    bold: true,
                    margin: [0, 5, 0, 2]
                },
                tableHeader: {
                    bold: true,
                    fontSize: 11,
                    color: 'black'
                },
                total: {
                    fontSize: 14,
                    bold: true
                },
                italicText: {
                    italics: true,
                    color: '#333333'
                }
            }
        };

        const pdfDoc = printer.createPdfKitDocument(documentDefinition);

        res.setHeader('Content-Type', 'application/pdf');
        res.setHeader('Content-Disposition', `attachment; filename=invoice-${orderData.orderId || 'unknown'}.pdf`);

        pdfDoc.pipe(res);
        pdfDoc.end();

    } catch (error) {
        console.error("Error generating PDF invoice:", error.message);
        console.error("Stack trace:", error.stack);
        const errorMessage = process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'dev'
                            ? `Lỗi máy chủ khi tạo hóa đơn PDF: ${error.message}`
                            : 'Lỗi máy chủ khi tạo hóa đơn PDF. Vui lòng thử lại sau.';
        res.status(500).json({
            message: errorMessage,
            ...( (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'dev') && { stack: error.stack } )
        });
    }
};