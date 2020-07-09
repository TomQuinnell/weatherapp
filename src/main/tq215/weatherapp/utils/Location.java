package main.tq215.weatherapp.utils;

import java.util.List;

public class Location {
    private String name;
    private double lat;
    private double lon;
    private String latlon;
    private ForecastAtTime currentForecast;
    private ForecastComposite twelveHour;
    private ForecastComposite sevenDay;

    public Location(String name, double lat, double lon) {
        // no forecast found/needed
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.currentForecast = new ForecastAtTime(false);
        this.twelveHour = new ForecastComposite(12);
        this.sevenDay = new ForecastComposite(7);
        this.latlon = lat + "_" + lon;
    }

    public String getName() {
        return name;
    }

    public double getLat() { return lat; }

    public double getLon() { return lon; }

    public String getLatlon() { return latlon;}

    public ForecastAtTime getCurrentForecast() {
        return currentForecast;
    }

    public ForecastComposite getTwelveHour() {
        return twelveHour;
    }

    public ForecastComposite getSevenDay() {
        return sevenDay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLatLon(String latlon) {
        this.latlon = latlon;
    }

    public void setCurrentForecast(ForecastAtTime currentForecast) {
        this.currentForecast = currentForecast;
    }

    public void setTwelveHour(ForecastComposite twelveHour) {
        this.twelveHour = twelveHour;
    }

    public void setSevenDay(ForecastComposite sevenDay) {
        this.sevenDay = sevenDay;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                //", currentForecast=" + currentForecast +
                //", twelveHour=" + twelveHour +
                //", sevenDay=" + sevenDay +
                '}';
    }
}
