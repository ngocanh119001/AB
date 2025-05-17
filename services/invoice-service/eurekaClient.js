// eurekaClient.js
const { Eureka } = require('eureka-js-client');

// Lấy hostname hoặc IP của máy host (nếu cần, nhưng localhost thường đủ)
// Hoặc đơn giản là hardcode 'localhost' vì bạn đã map port ra localhost
const hostAccessibleHostName = 'localhost';
const hostAccessiblePort = 3000; // Port đã được map ra máy host

const client = new Eureka({
  instance: {
    app: 'invoice-service',
    instanceId: 'invoice-service-1', // Có thể tạo ID động nếu muốn chạy nhiều instance
    hostName: 'invoice-service',      // Tên host DÙNG BÊN TRONG Docker network cho service discovery
    ipAddr: 'invoice-service',        // IP (dùng tên host) DÙNG BÊN TRONG Docker network
    port: {
      '$': 3000,                    // Port DÙNG BÊN TRONG container
      '@enabled': true
    },
    vipAddress: 'invoice-service',    // Tên logic để các client khác tìm kiếm service này
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn'
    },
    // --- URLs CÓ THỂ TRUY CẬP TỪ BÊN NGOÀI (máy host) ---
    statusPageUrl: `http://${hostAccessibleHostName}:${hostAccessiblePort}/info`, // URL hiển thị trên UI, truy cập từ trình duyệt host
    healthCheckUrl: `http://${hostAccessibleHostName}:${hostAccessiblePort}/info`, // URL Eureka server có thể dùng để check health (tùy cấu hình Eureka)
    homePageUrl: `http://${hostAccessibleHostName}:${hostAccessiblePort}/`,         // URL trang chủ của service (tùy chọn)
  },
  eureka: {
    host: 'eureka-server', // Tên service Eureka bên trong Docker network
    port: 8761,
    servicePath: '/eureka/apps/',
    maxRetries: 10,
    requestRetryDelay: 2000
  }
});

module.exports = client;