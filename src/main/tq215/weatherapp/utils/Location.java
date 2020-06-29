package main.tq215.weatherapp.utils;

public class Location {
    private String name;
    private String id;
    private Forecast forecast;

    public Location(String name, String id, Forecast forecast) {
        this.name = name;
        this.id = id;
        this.forecast = forecast; // forecast found
    }

    public Location(String name, String id) {
        this.name = name;
        this.id = id;
        this.forecast = null; // no forecast found/needed
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
