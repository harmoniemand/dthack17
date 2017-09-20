
//var leafletCss = require('./../node_modules/leaflet/dist/leaflet.css');
var css = require('./scss/main.scss');

var angular = require('angular');
//var leaflet = require('leaflet');
//var leafletng = require('angular-leaflet-directive');


angular.module('rodar', [])
    .controller('mapController', require('./controllers/map.controller.js'));

angular.element(function() {
    angular.bootstrap(document, ['rodar']);
});