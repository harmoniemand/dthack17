
// CSS
require('./../node_modules/leaflet/dist/leaflet.css');
require('./scss/main.scss');

// Assets
require('./assets/marker-icon.png');
require('./assets/marker-icon-2x.png');
require('./assets/marker-shadow.png');
require('./assets/favicon.png');

// DEPENDENCIES
var angular = require('angular');
// var leaflet = require('leaflet');

// Configure Angular
angular.module('rodar', [])
    .controller('sitebarController', require('./controllers/sitebar.controller.js'))
    .controller('mapController', require('./controllers/map.controller.js'));

// Bootstrap App
angular.element(function() {
    angular.bootstrap(document, ['rodar']);
});