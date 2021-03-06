var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var cors = require('cors');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use(cors());

var port = process.env.PORT || 8080;        // set our port

var router = express.Router();              // get an instance of the express Router

app.use('/', express.static('static'));

router.get('/users', require('./routes/users.js').get);
router.post('/users', require('./routes/users.js').post);

router.get('/sensors', require('./routes/sensors.js').get);
router.post('/sensors', require('./routes/sensors.js').post);

app.use('/api', router);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);