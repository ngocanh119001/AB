# Dùng image Node.js chính thức làm base image
FROM node:21

# Set thư mục làm việc
WORKDIR /usr/src/app

# Copy package.json và package-lock.json vào container
COPY package*.json ./

# Cài đặt các dependencies
RUN npm install

# Copy tất cả các file còn lại vào container
COPY . .

# Mở port 3000
EXPOSE 3000

# Lệnh chạy ứng dụng
CMD ["npm", "start"]
