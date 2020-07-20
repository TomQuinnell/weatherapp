package main.tq215.weatherapp;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.Back.LocationCache;
import main.tq215.weatherapp.Front.BigForecast;
import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.Front.SearchResults;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Main is the class in which we launch our psvm method

    // JPanels of the screens of the GUI
    private static HomePage homePage;
    private static SearchResults searchResults;
    private static BigForecast bigForecast;

    // locationCache to store recents
    private static LocationCache locationCache;

    // JFrame to display current screen to user
    private JFrame masterFrame;

    // list of Locations on startup, and user's location
    private static final List<Location> defaultRecents = List.of(new Location("Paris", 48.87, 2.33),
                                                                new Location("Rome", 41.9, 12.48),
                                                                new Location("London", 51.52, -0.12),
                                                                new Location("Edinburgh", 55.95, -3.16),
                                                                new Location("Barcelona", 41.38, 2.18));
    private static final Location userLocation = new Location("London", 51.52,-0.12); // TODO set user location in app

    public Main() {
        // constructor for Main() class

        // create location cache, initially filled with a few defaults
        locationCache = new LocationCache();
        locationCache.add(userLocation.getLatlon(), userLocation.getName());
        for (Location l: defaultRecents) {
            // check to see if user location equal to a default
            if (!l.getLatlon().equals(userLocation.getLatlon())) {
                locationCache.add(l.getLatlon(), l.getName());
            }
        }

        // set up Master Frame
        masterFrame = new JFrame("Weather App");
        masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // set up homepage Panel
        homePage = new HomePage(locationCache.getTopK(4), getUserLocation());
        homePage.addSearchBarAL(makeHomePageSearchBarAL());

        // set up searchResults Panel
        searchResults = new SearchResults(new ArrayList<>());
        searchResults.addBackButtonAL(makeSearchBackButtonAL());

        // set up bigSnapshot Panel
        bigForecast = new BigForecast(new Location("User shouldn't see this (I hope)", 1000000000.0, 1000000000.0));

        // display the home page
        displayHomePage();
    }

    public static void main(String[] args) {
        // method which gets run, loads a new Main() and lets it whirl away
        new Main();
    }

    private void setMasterFrameContent(JPanel panel) {
        // set the contents of the master JFrame to the JPanel pane;
        this.masterFrame.getContentPane().removeAll();
        this.masterFrame.getContentPane().repaint();
        this.masterFrame.add(panel);
        this.masterFrame.pack();
        this.masterFrame.setVisible(true);
    }

    private Location getUserLocation() {
        // get user location from locationCache
        return locationCache.findLocation(userLocation.getLatlon(), userLocation.getName(), false);
    }

    private ActionListener makeHomePageExpandAL(Location l) {
        // make expand button ActionListener for Location l
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayFullPage(l);
            }
        };
    }

    private ActionListener makeHomePageSearchBarAL() {
        // make Action Listener for HomePage search bar
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // get query typed into search bar
                String query = homePage.getSearchBarText();

                // update based on its results
                displaySearchResults(query);
            }
        };
    }

    private ActionListener makeBigForecastBackAL(Location l) {
        // make ActionListener for BigForecast's back button
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (l != getUserLocation()) {
                    locationCache.add(l.getLatlon(), l.getName());
                }
                bigForecast.resetComposite();
                displayHomePage();
            }
        };
    }

    private ActionListener makeSearchBackButtonAL() {
        // make ActionListener for SearchResults' Back Button
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayHomePage();
            }
        };
    }

    private ActionListener makeSearchRowAL(Location l) {
        // make ActionListener for SearchResults' Button for location
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                displayFullPage(l);
            }
        };
    }

    private void displayHomePage() {
        // make sure homePage visible
        setMasterFrameContent(homePage);

        // get snapshot for user location and recents
        Backend.getSnapshot(getUserLocation());
        List<Location> recents = locationCache.getTopK(4);
        for(Location l: recents) {
            Backend.getSnapshot(l);
        }

        // update JPanel
        homePage.update(recents, getUserLocation());
        homePage.addListeners(makeHomePageExpandAL(getUserLocation()), makeHomePageExpandAL(recents.get(0)),
                makeHomePageExpandAL(recents.get(1)), makeHomePageExpandAL(recents.get(2)), makeHomePageExpandAL(recents.get(3)));
        homePage.revalidate();
        homePage.repaint();

        // repaint Master Frame
        this.masterFrame.revalidate();
        this.masterFrame.repaint();
    }

    private void displayFullPage(Location location) {
        // make sure bigForecast visible
        setMasterFrameContent(bigForecast);

        // get snapshot and 12 hour for location
        Backend.getSnapshot(location);
        Backend.get12Hour(location);

        // update JPanel
        bigForecast.update(location);
        bigForecast.setBackAL(makeBigForecastBackAL(location));
        bigForecast.revalidate();
        bigForecast.repaint();

        // repaint Master Frame
        this.masterFrame.revalidate();
        this.masterFrame.repaint();
    }

    private void displaySearchResults(String query) {
        // make sure bigForecast visible
        setMasterFrameContent(searchResults);

        // get searches
        List<Location> queryResults = Backend.getTopKSearches(query, 10, locationCache);

        // update JPanel
        searchResults.update(queryResults);
        for (int i = 0; i < queryResults.size(); i++) {
            searchResults.addResultActionListener(i, makeSearchRowAL(searchResults.getIthLocation(i)));
        }
        searchResults.revalidate();
        searchResults.repaint();

        // repaint Master Frame
        this.masterFrame.revalidate();
        this.masterFrame.repaint();    }
}
