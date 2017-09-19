
var JsonDB = require('node-json-db');
var db = new JsonDB("database", true, true);

module.exports = {
    push: db.push,
    getData: db.getData
};