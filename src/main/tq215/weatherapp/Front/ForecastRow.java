package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.ForecastAtTime;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

import static main.tq215.weatherapp.utils.SwingStuff.makeTextPane;

public class ForecastRow extends JPanel {
    private JTextPane time;
    private JLabel icon;
    private JTextPane temp;
    private JTextPane cloudCoverage;
    private JTextPane rain;
    private JTextPane humidity;
    private JTextPane windSpeed;

    private boolean isHour;

    public ForecastRow(ForecastAtTime forecast, boolean isHour) {
        setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // time
        LocalDateTime timeOfForecast = forecast.getTimeOfForecast();
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.isHour = isHour;
        if (timeOfForecast == null) {
            time = makeTextPane("Loading...\n");
        } else {
            if (isHour) {
                time = makeTextPane(timeOfForecast.getHour() + ":00");
            } else {
                time = makeTextPane(timeOfForecast.getDayOfWeek() + " " + timeOfForecast.getDayOfMonth() + "th");
            }
        }
        add(time, gbc);

        // forecast info
        gbc.gridx = 1;
        ImageIcon img = new ImageIcon(forecast.getPicture().getIcon());
        icon = new JLabel(img);
        add(icon, gbc);

        gbc.gridx = 2;
        temp = makeTextPane("Temp:      ");
        add(temp, gbc);

        gbc.gridx = 3;
        cloudCoverage = makeTextPane("Clouds:      ");
        add(cloudCoverage, gbc);

        gbc.gridx = 4;
        rain = makeTextPane("Rain:      ");
        add(rain, gbc);

        gbc.gridx = 5;
        humidity = makeTextPane("Humidity:      ");
        add(humidity, gbc);

        gbc.gridx = 6;
        windSpeed = makeTextPane("Wind Speed:      ");
        add(windSpeed, gbc);
    }

    public void update(ForecastAtTime newForecast) {
        // update state of row from new forecast

        // time
        LocalDateTime timeOfForecast = newForecast.getTimeOfForecast();
        if (isHour) {
            time.setText(timeOfForecast.getHour() + ":00");
        } else {
            time.setText(timeOfForecast.getDayOfWeek() + " " + timeOfForecast.getDayOfMonth() + "th");
        }

        // icon
        ImageIcon img = new ImageIcon(newForecast.getPicture().getIcon());
        icon.setIcon(img);

        // forecast info
        temp.setText("Temp: " + newForecast.getTemp());
        cloudCoverage.setText("Clouds: " + newForecast.getCloudCoverage());
        rain.setText("Rain: " + newForecast.getRain());
        humidity.setText("Humidity: " + newForecast.getHumidity());
        windSpeed.setText("Wind Speed: " + newForecast.getWindSpeed());
        revalidate();
        repaint();
    }
}
