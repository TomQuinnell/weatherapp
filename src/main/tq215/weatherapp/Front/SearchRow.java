package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;

public class SearchRow extends JButton {
    // SearchRow is a Button with a Location name on it. It opens up a BigForecast for the Location when clicked.
    private Location location;

    public SearchRow(Location location) {
        super(location.getName());
        this.location = location;
    }

    public Location getLoc() {
        return location;
    }
}
