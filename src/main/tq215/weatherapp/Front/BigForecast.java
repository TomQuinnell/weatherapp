package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.utils.ForecastComposite;
import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class BigForecast extends JPanel implements Updateable {
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
        isTwelveHour = true;
        composite = new TwelveHour(this.location);
        composite.addListener(make12HAL());
        add(composite, gbc);
    }

    public ActionListener make12HAL() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gbc.gridx = 0;
                gbc.gridy = 2;

                GUIForecastComposite newComposite = new SevenDay(location);
                newComposite.addListener(make7DAL());

                remove(composite);
                composite = newComposite;
                add(composite, gbc);
                revalidate();
                repaint();

                Backend.get7Day(location);
                composite.update();
                revalidate();
                repaint();
            }
        };
    }

    public ActionListener make7DAL() {
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

                Backend.get12Hour(location); // TODO make this (and 12HAL call) async
                composite.update();
                revalidate();
                repaint();
            }
        };
    }

    public void update(Location location) {
        this.location = location;

        snapshot.update(location);
        composite.update(location);
        revalidate();
        repaint();
    }

    public void update() {
        snapshot.update();
        composite.update();
        revalidate();
        repaint();
    }

    public void setBackAL(ActionListener listener) {
        for (ActionListener l: this.backButton.getActionListeners()) {
            this.backButton.removeActionListener(l);
        }
        this.backButton.addActionListener(listener);
    }

    public void resetComposite() {
        remove(composite);
        this.composite = new TwelveHour(this.location);
        this.composite.addListener(make12HAL());
        add(composite, gbc);
    }
}
