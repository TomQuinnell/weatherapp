package tq215.weatherapp.Back;

import main.tq215.weatherapp.Back.LocationCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.utils.Location;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(JUnit4.class)
public class apiTest {

    @Test
    public void runQuery_london_current() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.getSnapshot(london);
        /*
        {"location":{"name":"London","region":"City of London, Greater London","country":"United Kingdom","lat":51.52,"lon":-0.11,
                "tz_id":"Europe/London","localtime_epoch":1593377917,"localtime":"2020-06-28 21:58"},
            "current":{"last_updated_epoch":1593377143,"last_updated":"2020-06-28 21:45","temp_c":16.0,"temp_f":60.8,"is_day":0,
                "condition":{"text":"Clear","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":11.9,
                "wind_kph":19.1,"wind_degree":230,"wind_dir":"SW","pressure_mb":1010.0,"pressure_in":30.3,"precip_mm":0.0,"precip_in":0.0,
                "humidity":68,"cloud":0,"feelslike_c":16.0,"feelslike_f":60.8,"vis_km":10.0,"vis_miles":6.0,"uv":1.0,"gust_mph":18.1,
                "gust_kph":29.2}}
         */
    }

    @Test
    public void runQueryTwice_checkFreshness_londonlatlong_current() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);

        Backend.getSnapshot(london);
        System.out.println(london.getCurrentForecast());
        LocalDateTime queryTime1 = london.getCurrentForecast().getTimeOfQuery();
        System.out.println();
        /*
        {"location":{"name":"London","region":"City of London, Greater London","country":"United Kingdom","lat":51.52,"lon":-0.11,
                "tz_id":"Europe/London","localtime_epoch":1593377917,"localtime":"2020-06-28 21:58"},
            "current":{"last_updated_epoch":1593377143,"last_updated":"2020-06-28 21:45","temp_c":16.0,"temp_f":60.8,"is_day":0,
                "condition":{"text":"Clear","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":11.9,
                "wind_kph":19.1,"wind_degree":230,"wind_dir":"SW","pressure_mb":1010.0,"pressure_in":30.3,"precip_mm":0.0,"precip_in":0.0,
                "humidity":68,"cloud":0,"feelslike_c":16.0,"feelslike_f":60.8,"vis_km":10.0,"vis_miles":6.0,"uv":1.0,"gust_mph":18.1,
                "gust_kph":29.2}}
         */
        Backend.getSnapshot(london);
        System.out.println(london.getCurrentForecast());
        LocalDateTime queryTime2 = london.getCurrentForecast().getTimeOfQuery();
        assert queryTime1 == queryTime2;
    }

    @Test
    public void runQuery_london_12h() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.get12Hour(london);

        System.out.println(london.getTwelveHour());
    }

    @Test
    public void runQueryTwice_checkFreshness_londonlatlong_12h() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.get12Hour(london);
        System.out.println(london.getTwelveHour());
        LocalDateTime queryTime1 = london.getTwelveHour().getTimeOfQuery();

        Backend.get12Hour(london);
        System.out.println(london.getTwelveHour());
        LocalDateTime queryTime2 = london.getTwelveHour().getTimeOfQuery();
        assert queryTime1 == queryTime2;
    }

    @Test
    public void runQuery_london_7day() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.get7Day(london);

        System.out.println(london.getSevenDay());
    }

    @Test
    public void runQueryTwice_checkFreshness_londonlatlong_7d() {
        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.get7Day(london);
        System.out.println(london.getSevenDay());
        LocalDateTime queryTime1 = london.getSevenDay().getTimeOfQuery();

        Backend.get7Day(london);
        System.out.println(london.getSevenDay());
        LocalDateTime queryTime2 = london.getSevenDay().getTimeOfQuery();
        assert queryTime1 == queryTime2;
    }

    @Test
    public void runSearch_top5_lond() {
        List<Location> top5 = Backend.getTopKSearches("lond", 5);
        System.out.println(top5);
    }

    @Test
    public void runSearch_top5_franklin() {
        List<Location> top5 = Backend.getTopKSearches("franklin", 5);
        System.out.println(top5);
    }

    @Test
    public void runSearch_top20_lond() {
        List<Location> top5 = Backend.getTopKSearches("lond", 20);
        System.out.println(top5);
    }

    @Test
    public void runSearch_top20_franklin() {
        List<Location> top5 = Backend.getTopKSearches("franklin", 20);
        System.out.println(top5);
    }

    @Test
    public void cleanQuery_noSpace() {
        String s = "!£$%^ihope&*this()works";
        String goal = "ihopethisworks";

        String cleaned = Backend.cleanQuery(s);
        System.out.println(cleaned);
        assert cleaned.equals(goal);
    }

    @Test
    public void cleanQuery_spaces() {
        String s = "!£$%^ihope&*this( )works";
        String goal = "ihopethis&20works";

        String cleaned = Backend.cleanQuery(s);
        System.out.println(cleaned);
        assert cleaned.equals(goal);
    }
}
