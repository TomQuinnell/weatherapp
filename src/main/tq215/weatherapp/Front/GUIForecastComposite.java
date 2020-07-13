package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;

public abstract class GUIForecastComposite extends JPanel {
    public abstract void update(Location newLocation);
    public abstract void update();

}
