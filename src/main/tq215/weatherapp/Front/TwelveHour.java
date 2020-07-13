package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.ForecastComposite;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static main.tq215.weatherapp.utils.SwingStuff.makeTextPane;

public class TwelveHour extends GUIForecastComposite {
    private JTextPane forecastName;
    private JButton switchButton;
    private List<ForecastRow> forecasts;
    private Location location;

    public TwelveHour(Location location) {
        this.location = location;
        ForecastComposite twelveHour = location.getTwelveHour();

        setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;

        // name of forecast
        gbc.gridx = 0;
        gbc.gridy = 0;
        forecastName = makeTextPane("12 Hour Forecast");
        add(forecastName, gbc);

        // button to switch to 12 hour
        gbc.gridx = 1;
        gbc.gridy = 0;
        switchButton = new JButton("7 Day Forecast");
        add(switchButton, gbc);

        // forecasts
        gbc.ipadx = 2;
        gbc.ipady = 2;
        forecasts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ForecastRow currentForecast = new ForecastRow(twelveHour.getIthForecast(i), true);
            forecasts.add(currentForecast);
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(currentForecast, gbc);
        }
    }

    @Override
    public void update(Location newLocation) {
        // update forecast info from new twelve hour from location
        this.location = newLocation;
        update();
    }

    @Override
    public void update() {
        // update forecast info from new twelve hour
        ForecastComposite newTwelveHour = location.getTwelveHour();
        for (int i = 0; i < 12; i++) {
            ForecastAtTime newForecast = newTwelveHour.getIthForecast(i);
            forecasts.get(i).update(newForecast);
        }
    }
}
