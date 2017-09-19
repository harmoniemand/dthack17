const dgram = require('dgram');
const server = dgram.createSocket('udp4');
var mysql = require('mysql');
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');

function isJson(obj)
{
    try
    {
        JSON.parse(obj);
        return true;
    }
    catch (e)
    {
        console.log("not JSON");
        return false;
    }
}

var con = mysql.createConnection({
  host: "localhost",
  user: process.env.dthack17_db_user || "dthack17",
  password: process.env.dthack17_db_passsword || "dthack17",
  database: process.env.dthack17_db_name || "dthack17"
});

server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
});

// Connection URL
var url = 'mongodb://localhost:27017/dthack17';

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db)
{
    assert.equal(null, err);
    console.log("Connected successfully to server");

    server.on('message', (msg, rinfo) =>
    {
        //console.log(msg);
        if(isJson(msg))
        {
            var collection = db.collection('documents');
            // Insert some documents
            collection.insertMany(
                [msg],
                function(err, result)
                {
                    console.log("Inserted 1 documents into the collection");
                });
        }
    });

    db.close();
});

/*con.connect(function(err)
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
});*/

server.on('listening', () => {
    const address = server.address();
    console.log(`server listening ${address.address}:${address.port}`);
});

server.bind(2017);