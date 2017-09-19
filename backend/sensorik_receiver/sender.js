const dgram = require('dgram');
const message = Buffer.from(JSON.stringify({'test':'test'}));
const client = dgram.createSocket('udp4');
client.send(message, 2017, 'localhost', (err) => {
  client.close();
});