
//var L = require('leaflet');

module.exports = ['$scope', '$http', function (scope, $http) {

    $http({
        method: 'GET',
        url: 'http'
    }).then(function successCallback(response) {
        // this callback will be called asynchronously
        // when the response is available
    }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
    });

    var data = [
        {
            lat: 50.7067745,
            long: 7.1289442,
            acc: 1.7,
            device: 'android'
        }
    ];


    var map = L.map('map').setView([50.7067745, 7.1289442], 13);

    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    data.forEach(function (item) {
        L.marker([item.lat, item.long])
            .addTo(map)
            .bindPopup('ACC: ' + item.acc + " - DEVICE: " + item.device)
            .openPopup();
    })

}];