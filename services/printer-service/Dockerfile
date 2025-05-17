FROM node:21

WORKDIR /usr/src/app/invoice-service

COPY package*.json ./
RUN npm install

COPY fonts ./fonts

COPY . .

EXPOSE 3002
CMD ["npm", "start"]