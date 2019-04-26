map1 = null;
map2 = null;
var app = {
   // Application Constructor
   initialize: function () {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
   },

   // deviceready Event Handler
   //
   // Bind any cordova events here. Common events are:
   // 'pause', 'resume', etc.
   onDeviceReady: function () {
      this.receivedEvent('deviceready');
      this.clearCache();
      loadMapAndGetData();
   },
   clearCache: function () {
      var success = function (status) {
         // alert('Message: ' + status);
      };
      var error = function (status) {
         alert('Error: ' + status);
      };
      if (window.CacheClear)
         window.CacheClear(success, error);
   },
   onSuccess: function (position) {
      alert('Latitude: ' + position.coords.latitude + '\n' +
         'Longitude: ' + position.coords.longitude + '\n' +
         'Altitude: ' + position.coords.altitude + '\n' +
         'Accuracy: ' + position.coords.accuracy + '\n' +
         'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
         'Heading: ' + position.coords.heading + '\n' +
         'Speed: ' + position.coords.speed + '\n' +
         'Timestamp: ' + position.timestamp + '\n');
   },

   // onError Callback receives a PositionError object
   //
   onError: function onError(error) {
      alert('code: ' + error.code + '\n' +
         'message: ' + error.message + '\n');
   },


   // Update DOM on a Received Event
   receivedEvent: function (id) {
      var parentElement = document.getElementById(id);
      var listeningElement = parentElement.querySelector('.listening');
      var receivedElement = parentElement.querySelector('.received');

      listeningElement.setAttribute('style', 'display:none;');
      receivedElement.setAttribute('style', 'display:block;');

      console.log('Received Event: ' + id);
   }
};

nearbyMarkers = [];
nearbyPositions = [];
function updateMapsData(position) {

   var pos = {
      lat: position.coords.latitude,
      lng: position.coords.longitude
   };

   getWeather(pos);

   if (map1 != null) {

      map1.setCameraZoom(15);
      map1.moveCamera({
         zoom: 15,
         tilt: 90,
         bearing: 0,
      });
      map1.setCameraTarget(pos);
      map1.addMarker({
         'position': pos,
         'title': "Mylocation"
      });


      let url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + pos.lat + "," + pos.lng + "&radius=500&key=" + apikey;
      $.get(url).done((data) => {

         data.results.forEach(element => {
            var marker = {
               'position': element.geometry.location,
               'title': element.name
            }
            nearbyPositions.push(element.geometry.location);
            var addedMarker = map2.addMarker(marker);
            nearbyMarkers.push(addedMarker);
         });

         if (nearbyPositions && nearbyPositions.length) { //Pārāk bieži testējot POI neielādējas Google API ierobežojuma dēļ;
            latlngbounds = new plugin.google.maps.LatLngBounds(nearbyPositions);
            var map2Pos = latlngbounds.getCenter();
            // alert(JSON.stringify(map2Pos));
            map2.moveCamera({
               target: latlngbounds,
            });
            map2.setCameraTarget(map2Pos);
            // alert(JSON.stringify(data.results));
         } else {
            alert("center not found")
         }
      });

   } else {
      alert('Map objekts nav inicializēts');
      alert('Latitude: ' + position.coords.latitude + '\n' +
         'Longitude: ' + position.coords.longitude + '\n' +
         'Altitude: ' + position.coords.altitude + '\n' +
         'Accuracy: ' + position.coords.accuracy + '\n' +
         'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
         'Heading: ' + position.coords.heading + '\n' +
         'Speed: ' + position.coords.speed + '\n' +
         'Timestamp: ' + position.timestamp + '\n');
   }

}

function getPosition() {
   var options = {
      enableHighAccuracy: true,
      maximumAge: 3600000
   }
   var watchID = navigator.geolocation.getCurrentPosition(onSuccess, onError, options);

   function onSuccess(position) {
      updateMapsData(position);
   };

   function onError(error) {
      alert('code: ' + error.code + '\n' + 'message: ' + error.message + '\n');
   }
}
var marker = null;

//Netiek izmantots;
function watchPosition() {
   var options = {
      maximumAge: 3600000,
      timeout: 3000,
      enableHighAccuracy: true,
   }
   var watchID = navigator.geolocation.watchPosition(onSuccess, onError, options);

   function onSuccess(position) {
      updateMapsData(position);
   };

   function onError(error) {
      alert('code: ' + error.code + '\n' + 'message: ' + error.message + '\n');
   }
}

function getWeather(pos) {

   let appendUL = function (ul, txt) {
      ul.append('<li>' + txt + '</li>');
   }
   let ul = $("#weatherData");

   let url = "https://weather.cit.api.here.com/weather/1.0/report.json?product=observation&latitude=" + pos.lat + "&longitude=" + pos.lng + "&oneobservation=true&app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg";
   $.get(url)
      .done(
         (data) => {
            var a = data;

            appendUL(ul, a.observations.location[0].city + " (" + a.observations.location[0].country + ")");
            appendUL(ul, a.observations.location[0].observation[0].description);

            appendUL(ul, a.observations.location[0].observation[0].temperature + "C");
            appendUL(ul, a.observations.location[0].observation[0].lowTemperature + "C - " + a.observations.location[0].observation[0].highTemperature + "C");
            appendUL(ul, "Mitrums: " + a.observations.location[0].observation[0].humidity + " %");
            appendUL(ul, a.feedCreation);
         }
      )
      .fail(
         (err) => { alert("get request failed" + err) }
      );
}



function loadMapAndGetData() {
   var div = document.getElementById("map_canvas");
   var div2 = document.getElementById("map_canvas2");

   // plugin.google.maps.environment.setEnv({
   //    'API_KEY_FOR_BROWSER_RELEASE': 'AIzaSyC4MiQcVtsLX3SunNjmzCauca8BvTIidnU',
   //    'API_KEY_FOR_BROWSER_DEBUG': 'AIzaSyC4MiQcVtsLX3SunNjmzCauca8BvTIidnU'
   // });

   // Create a Google Maps native view under the map_canvas div.
   map1 = plugin.google.maps.Map.getMap(div);
   map2 = plugin.google.maps.Map.getMap(div2);

   // If you click the button, do something...
   getPosition();
}


app.initialize();