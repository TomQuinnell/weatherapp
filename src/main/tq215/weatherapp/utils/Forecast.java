package main.tq215.weatherapp.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Forecast {
    private Picture picture;
    private double temp; // in degrees C
    private double cloudCoverage; // percentage of cloud
    private double rain; // rain amount in mm
    private double humidity; // percentage of humidity
    private double windSpeed; // in mph
    private LocalDateTime timeOfForecast; // time object
    private LocalDateTime timeOfQuery; // time object

    private final static  int maxMinsBeforeNew = 5; // mins before new query needed

    public Forecast(double temp, double cloudCoverage, double rain, double humidity, double windSpeed, LocalDateTime timeOfForecast) {
        this.temp = temp;
        this.cloudCoverage = cloudCoverage;
        this.rain = rain;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timeOfForecast = timeOfForecast;
        updatePic();
        this.timeOfQuery = LocalDateTime.now();
    }

    private void updatePic() {
        // update picture based on weather of forecast
        double WARMLEVEL = 15.0;
        boolean isWarm = this.temp > WARMLEVEL;

        double CLOUDYLEVEL = 50.0;
        boolean isCloudy = this.cloudCoverage > CLOUDYLEVEL;

        double RAINYLEVEL = 50.0;
        boolean isRainy = this.rain > RAINYLEVEL;

        Picture picToUpdateTo;

        if (!isWarm) {
            // not warm, not cloudy, not rainy
            if (!isCloudy && !isRainy) {
                picToUpdateTo = Picture.COLD;
            }
            // not warm, not cloudy, rainy
            else if (!isCloudy) {
                picToUpdateTo = Picture.RAIN;
            }
            // not warm, cloudy, not rainy
            else if (!isRainy) {
                picToUpdateTo = Picture.CLOUD;
            }
            // not warm, cloudy, rainy
            else {
                picToUpdateTo = Picture.RAINCLOUD;
            }
        } else {
            // warm, not cloudy, not rainy
            if (!isCloudy && !isRainy) {
                picToUpdateTo = Picture.WARM;
            }
            // warm, not cloudy, rainy
            else if (!isCloudy) {
                picToUpdateTo = Picture.RAIN;
            }
            // warm, cloudy, not rainy
            else if (!isRainy) {
                picToUpdateTo = Picture.SUNCLOUD;
            }
            // warm, cloudy, rainy
            else {
                picToUpdateTo = Picture.SUNRAINCLOUD;
            }
        }
        setPicture(picToUpdateTo);
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getCloudCoverage() {
        return cloudCoverage;
    }

    public void setCloudCoverage(double cloudCoverage) {
        this.cloudCoverage = cloudCoverage;
    }

    public double getrain() {
        return rain;
    }

    public void setrain(double rain) {
        this.rain = rain;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public LocalDateTime getTimeOfForecast() {
        return timeOfForecast;
    }

    public void setTimeOfForecast(LocalDateTime timeOfForecast) {
        this.timeOfForecast = timeOfForecast;
    }

    public LocalDateTime getTimeOfQuery() {
        return timeOfQuery;
    }

    public void setTimeOfQuery(LocalDateTime timeOfQuery) {
        this.timeOfQuery = timeOfQuery;
    }

    public void updateForecast(double temp, double cloudCoverage, double rain, double humidity, double windSpeed) {
        this.temp = temp;
        this.cloudCoverage = cloudCoverage;
        this.rain = rain;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timeOfQuery = LocalDateTime.now();
        updatePic();
    }

    public boolean isFresh() {
        // check if past maxMinsBeforeNew minutes since last query for freshness
        return this.getTimeOfQuery().until(LocalDateTime.now(), ChronoUnit.MINUTES) < maxMinsBeforeNew;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "picture=" + picture +
                ",\n temp=" + temp +
                ",\n cloudCoverage=" + cloudCoverage +
                ",\n rain=" + rain +
                ",\n humidity=" + humidity +
                ",\n windSpeed=" + windSpeed +
                ",\n timeOfForecast=" + timeOfForecast +
                ",\n timeOfQuery=" + timeOfQuery +
                '}';
    }
}
