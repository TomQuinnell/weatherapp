package main.tq215.weatherapp.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ForecastAtTime extends Forecast {
    private Picture picture;
    private double temp; // in degrees C
    private double cloudCoverage; // percentage of cloud
    private double rain; // rain amount in mm
    private double humidity; // percentage of humidity
    private double windSpeed; // in mph
    private LocalDateTime timeOfForecast; // time object of when forecast is
    private boolean isSmallPic; // whether pic is small or big

    public ForecastAtTime(double temp, double cloudCoverage, double rain, double humidity, double windSpeed, LocalDateTime timeOfForecast, boolean isSmallPic) {
        // contructor of all weather attributes, no timeOfQuery so default to now()
        super(LocalDateTime.now());
        this.temp = temp;
        this.cloudCoverage = cloudCoverage;
        this.rain = rain;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.isSmallPic = isSmallPic;
        updatePic();
        this.timeOfForecast = timeOfForecast;
    }

    public ForecastAtTime(boolean isSmallPic) {
        // null on start up
        super();
        if (isSmallPic) {
            setPicture(Picture.LOADINGSMALL);
        } else {
            setPicture(Picture.LOADING);
        }
    }

    private void updatePic() {
        // TODO make pictures at night not sunny
        // update picture based on weather of forecast
        double WARMLEVEL = 15.0;
        boolean isWarm = this.temp > WARMLEVEL;

        double CLOUDYLEVEL = 50.0;
        boolean isCloudy = this.cloudCoverage > CLOUDYLEVEL;

        double RAINYLEVEL = 50.0;
        boolean isRainy = this.rain > RAINYLEVEL;

        Picture picToUpdateTo;

        if (isRainy) {
            if (isWarm) {
                if (isSmallPic) {
                    picToUpdateTo = Picture.SUNRAINCLOUDSMALL;
                } else {
                    picToUpdateTo = Picture.SUNRAINCLOUD;
                }
            } else {
                if (isSmallPic) {
                    picToUpdateTo = Picture.RAINSMALL;
                } else {
                    picToUpdateTo = Picture.RAIN;
                }            }
        } else {
            if (!isWarm && !isCloudy) {
                if (isSmallPic) {
                    picToUpdateTo = Picture.COLDSMALL;
                } else {
                    picToUpdateTo = Picture.COLD;
                }
            } else if (!isWarm) {
                if (isSmallPic) {
                    picToUpdateTo = Picture.CLOUDSMALL;
                } else {
                    picToUpdateTo = Picture.CLOUD;
                }
            } else if (!isCloudy) {
                if (isSmallPic) {
                    picToUpdateTo = Picture.WARMSMALL;
                } else {
                    picToUpdateTo = Picture.WARM;
                }
            } else {
                if (isSmallPic) {
                    picToUpdateTo = Picture.SUNCLOUDSMALL;
                } else {
                    picToUpdateTo = Picture.SUNCLOUD;
                }            }
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

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
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

    public LocalDateTime getTimeOfQuery() {
        return super.getTimeOfQuery();
    }

    public void setTimeOfQuery(LocalDateTime timeOfQuery) {
        super.setTimeOfQuery(timeOfQuery);
    }

    public LocalDateTime getTimeOfForecast() {
        return timeOfForecast;
    }

    public void setTimeOfForecast(LocalDateTime timeOfForecast) {
        this.timeOfForecast = timeOfForecast;
    }

    public void updateForecast(double temp, double cloudCoverage, double rain, double humidity, double windSpeed) {
        this.temp = temp;
        this.cloudCoverage = cloudCoverage;
        this.rain = rain;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        super.setTimeOfQuery(LocalDateTime.now());
        updatePic();
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
                ",\n timeOfQuery=" + super.getTimeOfQuery() +
                ",\n timeOfForecast=" + timeOfForecast +
                '}';
    }
}
