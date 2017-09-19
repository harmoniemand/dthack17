
var JsonDB = require('node-json-db');
var db = new JsonDB("database", true, true);

var get = function(req, res) {
    res.json(db.getData('/sensors'));
};

var post = function (req, res) {
    var acc = req.body.acceleration;
    var lat = req.body.latitude;
    var long = req.body.longitude;

    db.push("/sensors", [{ 
        longitude: long ,
        latitude: lat,
        acceleration: acc
    }], false);
    res.json({ it: 'is saved'});
};


module.exports = {
    get: get,
    post: post
};