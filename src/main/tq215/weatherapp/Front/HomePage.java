package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
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
        subLocation1.update(recents.get(0));
        subLocation2.update(recents.get(1));
        subLocation3.update(recents.get(2));
        subLocation4.update(recents.get(3));

    }
}
