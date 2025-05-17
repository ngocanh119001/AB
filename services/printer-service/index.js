const express = require('express');
const dotenv = require('dotenv');
const bodyParser = require('body-parser');
const invoiceRoutes = require('./routes/invoiceRoutes');
const eurekaClient = require('./eurekaClient');

dotenv.config();

const app = express();

app.use(bodyParser.json());

app.get('/', (req, res) => {
  res.send('Invoice Service is running!');
});

app.get('/info', (req, res) => {
  res.status(200).json({
    status: 'UP',
    details: {
      database: 'up',
      diskSpace: 'up'
    }
  });
});

app.get('/health', (req, res) => {
  res.status(200).json({
    status: 'UP',
    details: {
      database: 'up',
      diskSpace: 'up'
    }
  });
});

app.use('/api/print-order', invoiceRoutes);

app.use((req, res, next) => {
    res.status(404).json({ message: 'Endpoint not found' });
});

app.use((err, req, res, next) => {
    console.error("Unhandled error:", err);
    res.status(err.status || 500).json({
        message: err.message || 'Internal Server Error'
    });
});

app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', '*');
  next();
});

const PORT = process.env.PORT || 3002;

const server = app.listen(PORT, () => {
  console.log(`Invoice Service running on port ${PORT}`);

  eurekaClient.start(error => {
    if (error) {
      console.error('Eureka client failed to start: ', error);
    } else {
      console.log('Invoice Service registered with Eureka');
    }
  });
});

const gracefulShutdown = () => {
  console.log('Stopping Invoice Service...');
  eurekaClient.stop(err => {
    if (err) {
      console.error('Error stopping Eureka client:', err);
    } else {
      console.log('Invoice Service unregistered from Eureka.');
    }
    server.close(() => {
      console.log('Server closed.');
      process.exit(0);
    });
  });
};

process.on('SIGTERM', gracefulShutdown);
process.on('SIGINT', gracefulShutdown);