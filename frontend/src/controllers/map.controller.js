
//var L = require('leaflet');

module.exports = ['$scope', '$http', function (scope, $http) {
    $http({
        method: 'GET',
        url: 'https://dthack17.de/api/sensors'
    }).then(function successCallback(response) {

        var data = [];
        response.data.forEach(function(item) {
            if (!(item.device && item.latitude && item.longitude && item.acceleration))
                return;
            
            data.push({
                device: item.device,
                lat: item.latitude,
                long: item.longitude,
                acc: item.acceleration
            });
        });

        var map = L.map('map').setView([50.7067745, 7.1289442], 13);

        L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        }).addTo(map);

        data.forEach(function (item) {
            L.marker([item.lat, item.long])
                .addTo(map)
                .bindPopup('ACC: ' + item.acc + " - DEVICE: " + item.device)
                .openPopup();
        });
    }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
    });
}];