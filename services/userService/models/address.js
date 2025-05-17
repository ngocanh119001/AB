module.exports = class Address {
    constructor(addressId, recipientName, recipientPhone, recipientAddress, addressType) {
      this.addressId = addressId;                   // String
      this.recipientName = recipientName;           // String
      this.recipientPhone = recipientPhone;         // String
      this.recipientAddress = recipientAddress;     // String
      this.addressType = addressType;               // int (ví dụ: 0 = nhà riêng, 1 = công ty)
    }
  };
  