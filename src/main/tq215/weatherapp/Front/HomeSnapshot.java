package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Forecast;
import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;

import static main.tq215.weatherapp.utils.SwingStuff.makeTextPane;

public class HomeSnapshot extends JPanel {
    private Location location;

    private JTextPane nameBadge;
    private JLabel weatherImg;
    private JTextPane temp;
    private JTextPane cloud;
    private JTextPane rain;
    private JTextPane humidity;
    private JTextPane wind;
    private JButton expandButton;

    public HomeSnapshot(Location location) {
        this.location = location;
        ForecastAtTime snapshot = location.getCurrentForecast();

        setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Name
        nameBadge = makeTextPane(location.getName());
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameBadge, gbc);

        // image of weather
        ImageIcon img = new ImageIcon(snapshot.getPicture().getIcon());
        weatherImg = new JLabel(img);
        gbc.gridheight = 5;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(weatherImg, gbc);

        // weather info
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 1;

        temp = makeTextPane("Temp:     ");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(temp, gbc);

        cloud = makeTextPane("Clouds:     ");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(cloud, gbc);

        rain = makeTextPane("Rain:     ");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(rain, gbc);

        humidity = makeTextPane("Humidity:     ");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(humidity, gbc);

        wind = makeTextPane("Wind Speed:     ");
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(wind, gbc);

        // button to expand to full forecast
        expandButton = new JButton("+");
        // TODO add a listener or something so expand into big view onClick()
        gbc.gridx = 2;
        gbc.gridy = 0;
        add(expandButton, gbc);
    }

    public void update(Location location) {
        this.location = location;
        update();
    }

    public void update() {
        ForecastAtTime snapshot = location.getCurrentForecast();
        this.nameBadge.setText(this.location.getName());
        this.weatherImg.setIcon(new ImageIcon(snapshot.getPicture().getIcon()));
        this.temp.setText("Temp: " + snapshot.getTemp());
        this.cloud.setText("Clouds: " + snapshot.getCloudCoverage());
        this.rain.setText("Rain: " + snapshot.getRain());
        this.humidity.setText("Humidity: " + snapshot.getHumidity());
        this.wind.setText("Wind: " + snapshot.getWindSpeed());
    }
}
