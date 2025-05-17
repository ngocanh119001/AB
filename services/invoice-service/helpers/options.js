module.exports = {
    format: "A4",
    orientation: "portrait",
    border: "10mm",
    header: {
        height: "10mm",
        contents: '<div style="text-align: center;">Hóa Đơn</div>'
    },
    footer: {
        height: "10mm",
        contents: {
            default: '<div style="text-align: center;">Trang {{page}} / {{pages}}</div>',
        }
    }
};
