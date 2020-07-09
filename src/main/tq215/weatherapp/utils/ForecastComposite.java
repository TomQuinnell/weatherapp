package main.tq215.weatherapp.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForecastComposite extends Forecast {
    private List<ForecastAtTime> forecasts;

    public ForecastComposite(List<Double> temp, List<Double> cloudCoverage, List<Double> rain, List<Double> humidity,
                             List<Double> windSpeed, List<LocalDateTime> timeOfForecast) {
        super(LocalDateTime.now());
        // zip lists of forecast attributes
        this.forecasts = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            this.forecasts.add(new ForecastAtTime(temp.get(i), cloudCoverage.get(i), rain.get(i), humidity.get(i), windSpeed.get(i), timeOfForecast.get(i), true));
        }
    }

    public ForecastComposite(int size) {
        // become null on startup
        super(); // we only need the timeOfQuery here for isFresh() in Super.
        this.forecasts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.forecasts.add(new ForecastAtTime(true));
        }
    }

    public ForecastComposite(List<ForecastAtTime> forecasts) {
        super(LocalDateTime.now());
        this.forecasts = forecasts;
    }

    public ForecastAtTime getIthForecast(int i) {
        return forecasts.get(i);
    }

    public List<ForecastAtTime> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<ForecastAtTime> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public String toString() {
        return "ForecastComposite{" +
                "timeOfQuery" + super.getTimeOfQuery() +
                "\nforecasts=" + forecasts +
                '}';
    }
}
