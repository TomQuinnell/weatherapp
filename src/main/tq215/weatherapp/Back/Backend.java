package main.tq215.weatherapp.Back;

import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.ForecastComposite;
import org.json.JSONObject;
import org.json.JSONArray;

import main.tq215.weatherapp.utils.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

public class Backend {
    // load weatherAPI.com key, located at ./Resources/ApiKeys/weatherapikey.txt
    private static String weatherapiKey = null;
    static {
        try {
            weatherapiKey = loadKey("./Resources/ApiKeys/weatherapikey.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // create URL and the DTF for the API's results
    private static final String weatherapiURL = "http://api.weatherapi.com/v1/";
    private static final DateTimeFormatter weatherapiDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // load openweatherAPI.com key, located at ./Resources/ApiKeys/openweatherkey.txt
    private static String openweatherKey = null;
    static {
        try {
            openweatherKey = loadKey("./Resources/ApiKeys/openweatherkey.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // create URL for API
    private static final String openweatherURL = "https://api.openweathermap.org/data/2.5/";

    private static String loadKey(String filePath) throws IOException {
        // load key from first line of .txt file at filePath
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String key = reader.readLine();
        reader.close();

        return key;
    }

    private static CompletableFuture<String> getAsync(String uri) {
        // function to create a CompletableFuture for request and return the response
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        System.out.println("Request built for " + uri);

        // TODO ensure query correctly terminates - analyse error codes etc.

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static LocalDateTime dtToLDT(long dt, int timezone_offset) {
        // convert dt long (since time universal time start) to LDT object (ensuring time zone taken into account)
        return LocalDateTime.ofEpochSecond(dt, 0, ZoneOffset.ofTotalSeconds(timezone_offset));
    }

    private static ForecastAtTime forecast_FromLiveJSON(JSONObject liveData, int timezone_offset, boolean isSmallPic) {
        // parse from live JSON data

        // double temp, double cloudCoverage, double rain, double humidity, double windSpeed
        double newTemp = (double) (Math.round(100.0 * (liveData.getDouble("temp") - 273.0)) / 100); // temp in Kelvin
        double newCloudCov = liveData.getDouble("clouds");
        double newRain; // NOTE: rain not always available :(
        if (liveData.has("rain") && liveData.getJSONObject("rain").has("1h")) {
            newRain = liveData.getJSONObject("rain").getDouble("1h");
        } else {
            newRain = 0.0;
        }
        double newHumidity = liveData.getDouble("humidity");
        double newWindSpeed = liveData.getDouble("wind_speed");
        long dt = liveData.getLong("dt");

        // create LocalDateTime object from dt
        LocalDateTime forecastTime = dtToLDT(dt, timezone_offset);

        return new ForecastAtTime(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed, forecastTime, isSmallPic);
    }

    private static ForecastAtTime forecast_FromDayJSON(JSONObject dayData, int timezone_offset) {
        // parse from day JSON data

        // double temp, double cloudCoverage, double rain, double humidity, double windSpeed
        double newTemp = (double) (Math.round(100.0 * (dayData.getJSONObject("temp").getDouble("max") - 273.0)) / 100); // temp in Kelvin
        double newCloudCov = dayData.getDouble("clouds");
        double newRain; // rain not always available :(
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

        return new ForecastAtTime(newTemp, newCloudCov, newRain, newHumidity, newWindSpeed, forecastTime, true);
    }

    public static void getSnapshot(Location location) {
        // get snapshot for location

        // first check for forecast existence and freshness if exists
        ForecastAtTime forecast = location.getCurrentForecast();
        if (forecast.notInitialised() || forecast.isOld()) {
            // first set location's forecast to LOADING
            location.setCurrentForecast(new ForecastAtTime(false));

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=minutely,hourly,daily" + "&appid=" + openweatherKey;
            //System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();

            //System.out.println(apiResult);


            // turn to JSONObject and parse out the relevant weather stats
            JSONObject jsonData = new JSONObject(apiResult);
            int timezone_offset = jsonData.getInt("timezone_offset");
            JSONObject current = jsonData.getJSONObject("current");

            // create new forecast and attach it to location
            ForecastAtTime updatedForecast = forecast_FromLiveJSON(current, timezone_offset, false);
            location.setCurrentForecast(updatedForecast);
        }
    }

    public static void get12Hour(Location location) {
        // get 12 hour forecast for location
        ForecastComposite forecast = location.getTwelveHour();

        // first check for forecast existence and freshness if exists
        if (forecast.notInitialised() || forecast.isOld() || forecast.isDifferentHour()) {
            // first set location's forecast to LOADING
            location.setTwelveHour(new ForecastComposite(12));

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=current,minutely,daily" + "&appid=" + openweatherKey;
            //System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();
            //System.out.println(apiResult);

            // turn to JSONObject and parse out the relevant weather stats
            List<ForecastAtTime> newTwelve = new ArrayList<>();
            JSONObject jsonData = new JSONObject(apiResult);
            int timezone_offset = jsonData.getInt("timezone_offset");
            JSONArray hourly = jsonData.getJSONArray("hourly");
            for (int i = 1; i <= 12; i++) { // i in [1, 12] to start at next hour
                // get ith hour
                JSONObject current = hourly.getJSONObject(i);

                ForecastAtTime forecastAtHour = forecast_FromLiveJSON(current, timezone_offset, true);
                newTwelve.add(forecastAtHour);
            }
            ForecastComposite newTwelveHour = new ForecastComposite(newTwelve);
            location.setTwelveHour(newTwelveHour);
        }
    }

    public static void get7Day(Location location) {
        // get 7 day forecast for location
        ForecastComposite forecast = location.getSevenDay();

        // first check for forecast existence and freshness if exists
        if (forecast.notInitialised() || forecast.isOld() || forecast.isDifferentDay()) {
            // first set location's forecast to LOADING
            location.setSevenDay(new ForecastComposite(7));

            // send off query to API
            // https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&
            //exclude=hourly,daily&appid={YOUR API KEY}
            String apiQuery = openweatherURL + "onecall?lat=" + location.getLat() + "&lon=" + location.getLon() + "&exclude=current,minutely,hourly" + "&appid=" + openweatherKey;
            //System.out.println("Sending call for snapshot for " + location.getName());

            CompletableFuture<String> httpCall = getAsync(apiQuery);
            String apiResult = httpCall.join();
            //System.out.println(apiResult);

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

    public static List<Location> getTopKSearches(String query, int k, LocationCache cache) {
        // get top K searches for query

        // ensure query has clean, non-API-code-injected, String
        String cleanQuery = cleanQuery(query);

        // send off query to API
        // http://api.weatherapi.com/v1/search.json?key=<YOUR_API_KEY>&q=lond
        String apiQuery = weatherapiURL + "search.json?key=" + weatherapiKey + "&q=" + cleanQuery;
        System.out.println("Sending call for search for " + cleanQuery);

        CompletableFuture<String> httpCall = getAsync(apiQuery);
        String apiResult = httpCall.join();
        //System.out.println(apiResult);

        // compile together top k results into a List
        List<Location> topk = new ArrayList<>();

        // parse JSON
        JSONArray searchResults = new JSONArray(apiResult);
        for (int i = 0; i < Math.min(searchResults.length(), k); i++) {
            JSONObject loc = searchResults.getJSONObject(i);
            double lat = loc.getDouble("lat");
            double lon = loc.getDouble("lon");
            String name = loc.getString("name");

            // add via LocationCache, to ensure reuse if exists
            topk.add(cache.findLocation(lat + "_" + lon, name, false));
        }

        return topk;
    }

    public static String cleanQuery(String query) {
        // first replace all chars in '^\\w ' (not {letters U space}), then replace space with &20 (special API char for ' '
        return query.replaceAll("[^\\w ]", "").replace(" ", "&20");
    }

    public static List<Location> getTopKSearches(String query, int k) {
        // ensure query has clean, non-API-code-injected, String
        String cleanQuery = cleanQuery(query);

        // send off query to API
        // http://api.weatherapi.com/v1/search.json?key=<YOUR_API_KEY>&q=lond
        String apiQuery = weatherapiURL + "search.json?key=" + weatherapiKey + "&q=" + cleanQuery;

        CompletableFuture<String> httpCall = getAsync(apiQuery);
        String apiResult = httpCall.join();
        //System.out.println(apiResult);

        List<Location> topk = new ArrayList<>();

        JSONArray searchResults = new JSONArray(apiResult);
        for (int i = 0; i < Math.min(searchResults.length(), k); i++) {
            JSONObject loc = searchResults.getJSONObject(i);
            double lat = loc.getDouble("lat");
            double lon = loc.getDouble("lon");
            String name = loc.getString("name");

            // Don't add via LocationCache, to ensure reuse if exists
            topk.add(new Location(name, lat, lon));
        }

        return topk;
    }
}
