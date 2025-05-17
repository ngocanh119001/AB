module.exports = class User {
    constructor(userId, tenNguoiDung, email, soDienThoai, roles, createdAt, updatedAt) {
      this.tenNguoiDung = tenNguoiDung;   // String
      this.email = email;                 // String
      this.soDienThoai = soDienThoai;     // String
      this.roles = roles || [];           // List<String>
      this.createdAt = createdAt || new Date();  // Date
      this.updatedAt = updatedAt || new Date();  // Date
    }
  };
  