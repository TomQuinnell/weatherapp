package main.tq215.weatherapp.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Forecast {
    private LocalDateTime timeOfQuery; // time object of when query sent

    private final static  int maxMinsBeforeNew = 5; // mins before new query needed

    public Forecast(LocalDateTime timeOfQuery) {
        // null on start up
        this.timeOfQuery = timeOfQuery;
    }

    public Forecast() {
        // null on start up
        timeOfQuery = null;
    }

    public LocalDateTime getTimeOfQuery() {
        return timeOfQuery;
    }

    public void setTimeOfQuery(LocalDateTime timeOfQuery) {
        this.timeOfQuery = timeOfQuery;
    }

    public boolean isOld() { // RENAME ELSEWHERE BRO
        // check if past maxMinsBeforeNew minutes since last query for freshness
        return this.timeOfQuery != null && this.timeOfQuery.until(LocalDateTime.now(), ChronoUnit.MINUTES) > maxMinsBeforeNew;
    }

    public boolean isDifferentHour() {
        // check if now in new hour
        return this.getTimeOfQuery().getHour() != LocalDateTime.now().getHour();
    }

    public boolean isDifferentDay() {
        // check if now on new day
        return this.getTimeOfQuery().getDayOfYear() != LocalDateTime.now().getDayOfYear();
    }

    public boolean notInitialised() {
        // check if not initialised yet (i.e. timeOfQuery null)
        return this.getTimeOfQuery() == null;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                ",\n timeOfQuery=" + timeOfQuery +
                '}';
    }
}
