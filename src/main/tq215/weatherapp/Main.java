package main.tq215.weatherapp;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.Back.LocationCache;
import main.tq215.weatherapp.Front.BigForecast;
import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main {
    private static HomePage homePage;
    private static JFrame homePageFrame;
    private static BigForecast bigForecast;
    private static JFrame bigForecastFrame;
    private static LocationCache locationCache;

    private static final List<Location> defaultRecents = List.of(new Location("Paris", 48.87, 2.33),
                                                                new Location("Rome", 41.9, 12.48),
                                                                new Location("London", 51.52, -0.12),
                                                                new Location("Edinburgh", 55.95, -3.16),
                                                                new Location("Barcelona", 41.38, 2.18));
    private static final Location userLocation = new Location("London", 51.52,-0.12);

    public Main() {
        // create location cache, initially filled with a few defaults
        locationCache = new LocationCache();
        locationCache.add(userLocation.getLatlon(), userLocation.getName());
        for (Location l: defaultRecents) {
            // check to see if user location equal to a default
            if (!l.getLatlon().equals(userLocation.getLatlon())) {
                locationCache.add(l.getLatlon(), l.getName());
            }
        }

        // set up homepage frame
        homePageFrame = new JFrame("Weather App");
        homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage = new HomePage(locationCache.getTopK(4), getUserLocation());
        homePageFrame.add(homePage);
        homePageFrame.pack();
        homePageFrame.setVisible(true);

        // set up bigSnapshot frame
        bigForecastFrame = new JFrame("Weather App");
        bigForecastFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bigForecast = new BigForecast(new Location("User shouldn't see this (I hope)", 1000000000.0, 1000000000.0));
        bigForecastFrame.add(bigForecast);
        bigForecastFrame.pack();
        bigForecastFrame.setVisible(false);
        displayHomePage();
    }

    public static void main(String[] args) {
        new Main();
    }

    public Location getUserLocation() {
        return locationCache.findLocation(userLocation.getLatlon(), userLocation.getName(), false);
    }

    private ActionListener makeHomePageAL(Location l) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayFullPage(l);
            }
        };
    }

    private ActionListener makeBigForecastAL(Location l) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                locationCache.add(l.getLatlon(), l.getName());
                displayHomePage();
            }
        };
    }

    public void displayHomePage() {
        // make sure homePage visible
        bigForecastFrame.setVisible(false);
        homePageFrame.setVisible(true);

        // get snapshot for user location and recents
        Backend.getSnapshot(getUserLocation());
        List<Location> recents = locationCache.getTopK(4);
        for(Location l: recents) {
            Backend.getSnapshot(l);
        }

        // update JPane
        homePage.update();
        homePage.addListeners(makeHomePageAL(getUserLocation()), makeHomePageAL(recents.get(0)),
                makeHomePageAL(recents.get(1)), makeHomePageAL(recents.get(2)), makeHomePageAL(recents.get(3)));
    }

    public void displayFullPage(Location location) {
        // make sure bigForecast visible
        homePageFrame.setVisible(false);
        bigForecastFrame.setVisible(true);

        // get snapshot and 12 hour for location
        Backend.getSnapshot(location);
        Backend.get12Hour(location);

        // update JPane
        bigForecast.update(location);
        bigForecast.setBackAL(makeBigForecastAL(location));
    }
}
