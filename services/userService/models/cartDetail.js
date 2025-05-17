module.exports = class CartDetail {
    constructor(productId, quantity, customerId, firstCategory, secondCategory) {
      this.productId = productId;           // String (ID của sản phẩm)
      this.quantity = quantity;             // Integer (Số lượng sản phẩm)
      this.customerId = customerId;         // String (ID của khách hàng)
      this.firstCategory = firstCategory;   // String (Danh mục chính của sản phẩm)
      this.secondCategory = secondCategory; // String (Danh mục phụ của sản phẩm)
    }
  };
  