
var JsonDB = require('node-json-db');
var db = new JsonDB("database", true, true);

var get = function(req, res) {
    res.json(db.getData('/users'));
};

var post = function (req, res) {
    var name = req.body.name;

    db.push("/users", [{ name: name }], false);
    res.json({ it: 'is saved'});
};


module.exports = {
    get: get,
    post: post
};