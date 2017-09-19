const dgram = require('dgram');
const server = dgram.createSocket('udp4');
var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: process.env.dbuser || "dthack17",
  password: process.env.dbpw || "dthack17",
  database: process.env.db || "dthack17"
});

server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
});

con.connect(function(err)
{
    con.query("CREATE TABLE IF NOT EXISTS test (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, test VARCHAR(255) NOT NULL)");
    if (err) throw err;
    server.on('message', (msg, rinfo) =>
    {
        console.log(`server got: ${msg} from ${rinfo.address}:${rinfo.port}`);
        con.query("SELECT * FROM test", function (err_, result, fields)
        {
            if (err_) throw err_;
            console.log(result);
            if(result.length == 0)
            {
                con.query("INSERT INTO `test` (`id`, `test`) VALUES (NULL, '123');");
            }
        });
    });
});

server.on('listening', () => {
    const address = server.address();
    console.log(`server listening ${address.address}:${address.port}`);
});

server.bind(2017);