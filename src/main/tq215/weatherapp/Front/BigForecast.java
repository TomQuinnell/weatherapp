package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.ForecastComposite;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;

public class BigForecast extends JPanel {
    private JButton backButton;
    private BigSnapshot snapshot;
    private GUIForecastComposite composite;
    private boolean isTwelveHour;
    private Location location;

    GridBagConstraints gbc = new GridBagConstraints();

    public BigForecast(Location location) {
        this.location = location;

        setLayout(new GridBagLayout());
        this.setBackground(Color.GRAY);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // back button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backButton = new JButton("Back");
        add(backButton, gbc);

        gbc.ipadx = 2;
        gbc.ipady = 2;

        // snapshot
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        snapshot = new BigSnapshot(this.location);
        add(snapshot, gbc);

        // composite forecast
        gbc.gridx = 0;
        gbc.gridy = 2;
        isTwelveHour = true;
        composite = new TwelveHour(this.location);
        add(composite, gbc);
    }

    public void update(Location location) {
        this.location = location;

        snapshot.updateState(location);
        remove(composite);
        gbc.gridx = 0;
        gbc.gridy = 2;
        isTwelveHour = !isTwelveHour;
        if (isTwelveHour) {
            composite = new TwelveHour(location);
        } else {
            composite = new SevenDay(location);
        }

        add(composite, gbc);
    }
}
