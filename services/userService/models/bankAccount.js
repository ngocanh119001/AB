module.exports = class BankAccount {
    constructor(bankNumber, dueDate, ownerName, address, zipCode) {
      this.bankNumber = bankNumber;   // String (Số tài khoản ngân hàng)
      this.dueDate = dueDate || new Date();  // Date (Ngày đáo hạn)
      this.ownerName = ownerName;     // String (Tên chủ tài khoản)
      this.address = address;         // String (Địa chỉ ngân hàng)
      this.zipCode = zipCode;         // int (Mã bưu điện)
    }
  };
  