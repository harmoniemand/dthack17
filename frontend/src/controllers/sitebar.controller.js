
//var L = require('leaflet');

module.exports = ['$scope', function (scope) {
    scope.close = function () {
        scope.$root.sitebarOpen = false;
    }
}];