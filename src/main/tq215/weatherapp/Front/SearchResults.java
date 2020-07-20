package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends JPanel {
    // SearchResults displays the search results for a query as a series of JButtons

    // components
    private List<SearchRow> buttons;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JButton backButton;

    public SearchResults(List<Location> results) {
        setLayout(new GridBagLayout());
        this.setBackground(new Color( 46, 134, 193 ));
        gbc.weightx = 1;
        gbc.weighty = 1;

        // back button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backButton = new JButton("Back");
        add(backButton, gbc);

        // blank space
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JPanel(), gbc);

        gbc.ipadx = 2;
        gbc.ipady = 2;

        // add buttons of results afterwards, below
        int yPos = 2;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        buttons = new ArrayList<>();
        for (Location location: results) {
            SearchRow newRow = new SearchRow(location);
            gbc.gridy = yPos;
            buttons.add(newRow);
            add(newRow, gbc);
            yPos++;
        }

        this.setPreferredSize(new Dimension(700, 700));
    }

    public void update(List<Location> results) {
        // update from list of Locations

        // first remove all buttons
        for (SearchRow row: this.buttons) {
            remove(row);
        }

        // add buttons of results afterwards, below
        int yPos = 2;
        gbc.gridx = 0;
        gbc.gridy = yPos;
        buttons = new ArrayList<>();
        for (Location location: results) {
            SearchRow newRow = new SearchRow(location);
            gbc.gridy = yPos;
            buttons.add(newRow);
            add(newRow, gbc);
            yPos++;
        }
        revalidate();
        repaint();
    }

    public void addBackButtonAL(ActionListener listener) {
        // add listener to back button
        this.backButton.addActionListener(listener);
    }

    public void addResultActionListener(int i, ActionListener listener) {
        // add a listener to the iTh button
        // decided to limit to in [0, this.buttons.size)
        if (i >= this.buttons.size()) {
            i = this.buttons.size() - 1;
        }
        if (i < 0) {
            i = 0;
        }
        this.buttons.get(i).addActionListener(listener);
    }

    public Location getIthLocation(int i) {
        return this.buttons.get(i).getLoc();
    }
}
