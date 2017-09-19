var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;        // set our port

var router = express.Router();              // get an instance of the express Router

app.use('/', express.static('static'));

router.get('/users', require('./routes/users.js').get);
router.post('/users', require('./routes/users.js').post);

router.post('/sensors', function (req, res) {
res.json({ it: 'works' });
});

app.use('/api', router);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);