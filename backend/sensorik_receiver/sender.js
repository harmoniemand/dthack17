const dgram = require('dgram');
const message = Buffer.from('Some bytes');
const client = dgram.createSocket('udp4');
client.send(message, 2017, 'localhost', (err) => {
  client.close();
});