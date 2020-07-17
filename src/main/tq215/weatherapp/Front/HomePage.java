package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class HomePage extends JPanel {
    private List<Location> recents;
    private Location userLocation;

    private JTextField searchBar;
    private HomeSnapshot mainLocation;
    private HomeSnapshot subLocation1;
    private HomeSnapshot subLocation2;
    private HomeSnapshot subLocation3;
    private HomeSnapshot subLocation4;

    public HomePage(List<Location> recents, Location userLocation) {
        this.recents = recents;
        this.userLocation = userLocation;

        setLayout(new GridBagLayout());
        this.setBackground(Color.RED);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(2, 2, 2, 2);

        // main location
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainLocation = new HomeSnapshot(userLocation);
        add(mainLocation, gbc);

        gbc.gridwidth = 1;
        // sub locations
        gbc.gridx = 0;
        gbc.gridy = 2;
        subLocation1 = new HomeSnapshot(recents.get(0));
        add(subLocation1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        subLocation2 = new HomeSnapshot(recents.get(1));
        add(subLocation2, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        subLocation3 = new HomeSnapshot(recents.get(2));
        add(subLocation3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        subLocation4 = new HomeSnapshot(recents.get(3));
        add(subLocation4, gbc);

        // search bar
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchBar = new JTextField();
        add(searchBar, gbc);
    }

    public void update(List<Location> recents, Location userLocation) {
        this.recents = recents;
        this.userLocation = userLocation;

        update();
    }

    public void update() {
        mainLocation.update(userLocation);
        subLocation1.update(recents.get(0)); // TODO make sure updates in Main/to recents are async so no java.util.ConcurrentModificationException raised here
        subLocation2.update(recents.get(1));
        subLocation3.update(recents.get(2));
        subLocation4.update(recents.get(3));
        revalidate();
        repaint();
    }

    public Location getMainLocation() {
        return mainLocation.getLoc();
    }

    public Location getSub1Location() {
        return subLocation1.getLoc();
    }

    public Location getSub2Location() {
        return subLocation2.getLoc();
    }

    public Location getSub3Location() {
        return subLocation3.getLoc();
    }

    public Location getSub4Location() {
        return subLocation4.getLoc();
    }

    public void addListeners(ActionListener mainListener, ActionListener sub1Listener, ActionListener sub2Listener,
                             ActionListener sub3Listener, ActionListener sub4Listener) {
        mainLocation.addListener(mainListener);
        subLocation1.addListener(sub1Listener);
        subLocation2.addListener(sub2Listener);
        subLocation3.addListener(sub3Listener);
        subLocation4.addListener(sub4Listener);
    }
}
