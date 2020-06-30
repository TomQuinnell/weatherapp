package main.tq215.weatherapp.Back;

import main.tq215.weatherapp.utils.Forecast;
import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.ForecastComposite;
import org.json.JSONObject;
import org.json.JSONArray;

import main.tq215.weatherapp.utils.Location;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

public class Backend {
    private static final String weatherapiKey = "";
    private static final String weatherapiURL = "http://api.weatherapi.com/v1/";
    private static final DateTimeFormatter weatherapiDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    private static final String openweatherKey = "";
    private static final String openweatherURL = "https://api.openweathermap.org/data/2.5/";

    private static final Random randomGenerator = new Random();

    private static CompletableFuture<String> getAsync(String uri) {
        // function to create a CompletableFuture for request and return the response
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        System.out.println("Request built for " + uri);

        // TODO ensure query correctly terminates

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static LocalDateTime dtToLDT(long dt, int timezone_offset) {
        return LocalDateTime.ofEpochSecond(dt, 0, ZoneOffset.ofTotalSeconds(timezone_offset));
    }

    private static ForecastAtTime forecast_FromLiveJSON(JSONObject liveData, int timezone_offset) {
        // double temp, double cloudCoverage, double rain, double humidity, double windSpeed
        double newTemp = (double) (Math.round(100.0 * (liveData.getDouble("temp") - 273.0)) / 100); // temp in Kelvin
        double newCloudCov = liveData.getDouble("clouds");
        double newRain;
        if (liveData.has("rain")) {
            newRain = liveData.getJSONObject("rain").getDouble("1h");
        } else {
            newRain = 0.0;
        }
        double newHumidity = liveData.getDouble("humidity");
        double newWindSpeed = liveData.getDouble("wind_speed");
        long dt = liveData.getLong("dt");

        // create LocalDateTime object from dt
        LocalDateTime forecastTime = dtToLDT(dt, timezone_offset);

        return new ForecastAtTime(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed, forecastTime);
    }

    private static ForecastAtTime forecast_FromDayJSON(JSONObject dayData, int timezone_offset) {
        // double temp, double cloudCoverage, double rain, double humidity, double windSpeed
        double newTemp = (double) (Math.round(100.0 * (dayData.getJSONObject("temp").getDouble("max") - 273.0)) / 100); // temp in Kelvin
        double newCloudCov = dayData.getDouble("clouds");
        double newRain; // rain not always available
        if (dayData.has("rain")) {
            newRain = dayData.getDouble("rain");
        } else {
            newRain = 0.0; // default to 0
        }
        double newHumidity = dayData.getDouble("humidity");
        double newWindSpeed = dayData.getDouble("wind_speed");
        long dt = dayData.getLong("dt");

        // create LocalDateTime object from dt
        LocalDateTime forecastTime = dtToLDT(dt, timezone_offset);

        return new ForecastAtTime(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed, forecastTime);
    }

    public static void getSnapshot(Location location) {
        // first check for forecast existence and freshness if exists
        ForecastAtTime forecast = location.getCurrentForecast();
        if (forecast.notInitialised() || forecast.isOld()) {
            // first set location's forecast to LOADING
            location.setCurrentForecast(new ForecastAtTime());

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=minutely,hourly,daily" + "&appid=" + openweatherKey;
            System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();

            System.out.println(apiResult);


            // turn to JSONObject and parse out the relevant weather stats
            JSONObject jsonData = new JSONObject(apiResult);
            int timezone_offset = jsonData.getInt("timezone_offset");
            JSONObject current = jsonData.getJSONObject("current");

            // create new forecast and attach it to location
            ForecastAtTime updatedForecast = forecast_FromLiveJSON(current, timezone_offset);
            location.setCurrentForecast(updatedForecast);
        }
    }

    public static void get12Hour(Location location) {
        // first check for forecast existence and freshness if exists
        ForecastComposite forecast = location.getTwelveHour();
        if (forecast.notInitialised() || forecast.isOld() || forecast.isDifferentHour()) {
            // first set location's forecast to LOADING
            location.setTwelveHour(new ForecastComposite(12));

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=current,minutely,daily" + "&appid=" + openweatherKey;
            System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();
            System.out.println(apiResult);

            // turn to JSONObject and parse out the relevant weather stats
            List<ForecastAtTime> newTwelve = new ArrayList<>();
            JSONObject jsonData = new JSONObject(apiResult);
            int timezone_offset = jsonData.getInt("timezone_offset");
            JSONArray hourly = jsonData.getJSONArray("hourly");
            for (int i = 1; i <= 12; i++) { // i in [1, 12] to start at next hour
                // get ith hour
                JSONObject current = hourly.getJSONObject(i);

                ForecastAtTime forecastAtHour = forecast_FromLiveJSON(current, timezone_offset);
                newTwelve.add(forecastAtHour);
            }
            ForecastComposite newTwelveHour = new ForecastComposite(newTwelve);
            location.setTwelveHour(newTwelveHour);
        }
    }

    public static void get7Day(Location location) {
        // first check for forecast existence and freshness if exists
        ForecastComposite forecast = location.getSevenDay();
        if (forecast.notInitialised() || forecast.isOld() || forecast.isDifferentDay()) {
            // first set location's forecast to LOADING
            location.setSevenDay(new ForecastComposite(7));

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=current,minutely,hourly" + "&appid=" + openweatherKey;
            System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();
            System.out.println(apiResult);

            // turn to JSONObject and parse out the relevant weather stats
            List<ForecastAtTime> newSeven = new ArrayList<>();
            JSONObject jsonData = new JSONObject(apiResult);
            int timezone_offset = jsonData.getInt("timezone_offset");
            JSONArray daily = jsonData.getJSONArray("daily");
            for (int i = 1; i <= 7; i++) { // i in [1, 7] to start at tomorrow
                // get ith hour
                JSONObject current = daily.getJSONObject(i);

                ForecastAtTime forecastOnDay = forecast_FromDayJSON(current, timezone_offset);
                newSeven.add(forecastOnDay);
            }
            ForecastComposite newSevenDay = new ForecastComposite(newSeven);
            location.setSevenDay(newSevenDay);
        }
    }

    public static List<Location> getTopKSearches(String query, int k) {
        // TODO ensure query has clean, non-API-code-injected, String
        // TODO ensure query not empty, if so return []

        // send off query to API
        // http://api.weatherapi.com/v1/search.json?key=<YOUR_API_KEY>&q=lond
        String apiQuery = weatherapiURL + "search.json?key=" + weatherapiKey + "&q=" + query;
        System.out.println("Sending call for search for " + query);

        CompletableFuture<String> httpCall = getAsync(apiQuery);
        String apiResult = httpCall.join();
        System.out.println(apiResult);

        List<Location> topk = new ArrayList<>();

        JSONArray searchResults = new JSONArray(apiResult);
        for (int i = 0; i < Math.min(searchResults.length(), k); i++) {
            JSONObject loc = searchResults.getJSONObject(i);
            double lat = loc.getDouble("lat");
            double lon = loc.getDouble("lon");
            String name = loc.getString("name");

            // TODO fetch from cache if exists first, then from JSON
            topk.add(new Location(name, lat, lon));
        }

        return topk;
    }

    public static void updateRecents(List<Location> recents, Location newLocation) {
        int a = 0;
    }
}
