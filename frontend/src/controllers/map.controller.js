
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
            var str = "";

            if (item.acc > 2) {
                str = "Stärke des Schadens: <span style='color: red'>HOCH</span> <br /> Gesendet von: " + item.device;
            } else if (item.acc > 1.5) {
                str = "Stärke des Schadens: <span style='color: orange'>MITTEL</span> <br /> Gesendet von: " + item.device;
            } else {
                str = "Stärke des Schadens: <span style='color: green'>GERING</span> <br /> Gesendet von: " + item.device;
            }
            
            L.marker([item.lat, item.long])
                .addTo(map)
                .bindPopup(str)
                .openPopup();
        });
    }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
    });
}];