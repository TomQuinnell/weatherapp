package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BigForecast extends JPanel implements Updateable {
    // BigForecast displays a snapshot of a forecast, alongside a larger composite (12 hour or 7 day) forecast

    // components
    private JButton backButton;
    private BigSnapshot snapshot;
    private GUIForecastComposite composite;
    private Location location;
    GridBagConstraints gbc = new GridBagConstraints();

    public BigForecast(Location location) {
        this.location = location;

        setLayout(new GridBagLayout());
        this.setBackground(new Color(46, 134, 193));
        //gbc.weightx = 1;
        //gbc.weighty = 1;

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
        composite = new TwelveHour(this.location);
        composite.addListener(make12HAL());
        add(composite, gbc);

        this.setPreferredSize(new Dimension(850, 800));
    }

    private ActionListener make12HAL() {
        // make an ActionListener to attach to the 12 hour forecast JPanel
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // switch to SevenDay()
                gbc.gridx = 0;
                gbc.gridy = 2;

                GUIForecastComposite newComposite = new SevenDay(location);
                newComposite.addListener(make7DAL());

                remove(composite);
                composite = newComposite;
                add(composite, gbc);
                revalidate();
                repaint();

                // fire off API call and update
                Backend.get7Day(location);
                composite.update();
                revalidate();
                repaint();
            }
        };
    }

    private ActionListener make7DAL() {
        // make an ActionListener to attach to the 12 hour forecast JPanel
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gbc.gridx = 0;
                gbc.gridy = 2;

                GUIForecastComposite newComposite = new TwelveHour(location);
                newComposite.addListener(make12HAL());

                remove(composite);
                composite = newComposite;
                add(composite, gbc);
                revalidate();
                repaint();

                // fire off API call
                Backend.get12Hour(location); // TODO make this (and 12HAL call) async
                composite.update();
                revalidate();
                repaint();
            }
        };
    }

    public void update(Location location) {
        // update from location
        this.location = location;

        snapshot.update(location);
        composite.update(location);
        revalidate();
        repaint();
    }

    public void update() {
        // update, as forecasts for location have been updated
        snapshot.update();
        composite.update();
        revalidate();
        repaint();
    }

    public void setBackAL(ActionListener listener) {
        // set back button action listener
        for (ActionListener l: this.backButton.getActionListeners()) {
            this.backButton.removeActionListener(l);
        }
        this.backButton.addActionListener(listener);
    }

    public void resetComposite() {
        // remove composite, switching back to an empty 12 hour
        remove(composite);
        this.composite = new TwelveHour(this.location);
        this.composite.addListener(make12HAL());
        add(composite, gbc);
    }
}
