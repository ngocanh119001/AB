const { Eureka } = require('eureka-js-client');
const os = require('os');
require('dotenv').config();

const PORT = process.env.PORT || 3002;
const HOST_ACCESSIBLE_HOSTNAME = process.env.HOST_ACCESSIBLE_HOSTNAME || 'localhost';
const SERVICE_NAME_INTERNAL = process.env.SERVICE_NAME || 'printer-service';


// Lấy địa chỉ IP thực
const getIpAddress = () => {
  const interfaces = os.networkInterfaces();
  // Thay 'eth0' bằng tên interface mạng của bạn nếu cần
  return interfaces['eth0']?.find(i => i.family === 'IPv4' && !i.internal)?.address || '127.0.0.1';
};

const client = new Eureka({
  instance: {
    app: SERVICE_NAME_INTERNAL,
    instanceId: `${SERVICE_NAME_INTERNAL}-${PORT}`,
    hostName: getIpAddress(),       // Hostname DÙNG BÊN TRONG Docker network
    ipAddr: getIpAddress(),         // IP (dùng tên host) DÙNG BÊN TRONG Docker network
    preferIpAddress: true,    // Bắt buộc Eureka dùng IP
    statusPageUrl: `http://${HOST_ACCESSIBLE_HOSTNAME}:${PORT}/info`,
    healthCheckUrl: `http://${HOST_ACCESSIBLE_HOSTNAME}:${PORT}/health`,
    homePageUrl: `http://${HOST_ACCESSIBLE_HOSTNAME}:${PORT}/`,
    port: {
      '$': PORT,
      '@enabled': true
    },
    vipAddress: SERVICE_NAME_INTERNAL,
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn'
    }
  },
  eureka: {
    host: process.env.EUREKA_HOST || 'eureka-server',
    port: process.env.EUREKA_PORT || 8761,
    servicePath: '/eureka/apps/',
    maxRetries: 10,
    requestRetryDelay: 2000
  }
});

module.exports = client;