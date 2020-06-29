package main.tq215.weatherapp.Back;

import main.tq215.weatherapp.utils.Forecast;
import org.json.JSONObject;
import org.json.JSONArray;

import main.tq215.weatherapp.utils.Location;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

public class Backend {
    private static final String apiKey = "";
    private static final String apiURL = "http://api.weatherapi.com/v1/";
    private static final int maxMinsBeforeNew = 5;

    private static CompletableFuture<String> getAsync(String uri) {
        // function to create a CompletableFuture for request and return the response
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        System.out.println("Request built for" + uri);

        // TODO ensure query correctly terminates

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static Forecast updateForecast_FromCurrentJSON(String jsonString, Forecast forecast) {
        // turn to JSONObject and parse out the relevant weather stats
        JSONObject jsonData = new JSONObject(jsonString);
        JSONObject current = jsonData.getJSONObject("current");
        // double temp, double cloudCoverage, double rain, double humidity, double windSpeed
        double newTemp = current.getDouble("temp_c");
        double newCloudCov = current.getDouble("cloud");
        double newRain = current.getDouble("precip_mm");
        double newHumidity = current.getDouble("humidity");
        double newWindSpeed = current.getDouble("gust_mph");

        if (forecast == null) {
            forecast = new Forecast(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed, LocalDateTime.now());
        } else {
            forecast.updateForecast(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed);
        }

        return forecast;
    }

    public static Forecast getSnapshot(Location location) {
        // first check for forecast existence and freshness if exists
        Forecast forecast = location.getForecast();
        if (forecast == null || !forecast.isFresh()) {
            // http://api.weatherapi.com/v1/current.json?key=<YOUR_API_KEY>&q=London
            // send off query to API
            String lat_long = location.getId();
            String apiQuery = apiURL + "current.json?key=" + apiKey + "&q=" + lat_long;
            System.out.println("Sending call...");
            /*
            CompletableFuture<String> httpCall = getAsync(apiQuery);

            String apiResult = httpCall.join();
            System.out.println(apiResult);
            */
            String apiResult = "{" + "location" + ":{" + "name" + ":" + "London" + "," + "region" + ":" + "City of London, Greater London" +
                    "," + "country" + ":" + "United Kingdom" + "," + "lat" + ": 51.52" + "," + "lon" + ":-0.11," +
                "tz_id" + ":"+ "Europe/London" + "," + "localtime_epoch" + ":1593377917," + "localtime" + ":" + "2020-06-28 21:58" + "}," +
            "current" + ":{" + "last_updated_epoch" + ":1593377143," + "last_updated" + ":" + "2020-06-28 21:45" + "," + "temp_c" + ":16.0," +
                     "temp_f" + ":60.8," + "is_day" + ":0," +
                "condition" + ":{" + "text" + ":" + "Clear" + "," + "icon" + ":" + "//cdn.weatherapi.com/weather/64x64/night/113.png" + "," +
                    "code" + ":1000}," + "wind_mph" + ":11.9," +
                "wind_kph" + ":19.1," + "wind_degree" + ":230," + "wind_dir" + ":" + "SW" + "," + "pressure_mb" + ":1010.0," + "pressure_in" +
                    ":30.3," + "precip_mm" + ":0.0," + "precip_in" + ": 0.0," +
                "humidity" + ":68," + "cloud" +":0," + "feelslike_c" + ":16.0," + "feelslike_f" + ":60.8," + "vis_km" + ":10.0," + "vis_miles" + ":6.0," + "uv" + ":1.0," + "gust_mph" + ":18.1," +
                "gust_kph" + ":29.2}}";

            Forecast updatedForecast = updateForecast_FromCurrentJSON(apiResult, forecast);
            location.setForecast(forecast);
        } else {
            System.out.println("Old query still fresh");
        }
        return null;
    }

    public static List<Forecast> get12Hour(Location location) {
        return null;
    }

    public static List<Forecast> get7Hour(Location location) {
        return null;
    }

    public static List<Location> getTopKSearches(String query, int k) {
        return null;
    }

    public static void updateRecents(List<Location> recents, Location newLocation) {
        int a = 0;
    }
}
