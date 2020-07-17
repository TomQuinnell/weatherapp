package main.tq215.weatherapp.Front;

import main.tq215.weatherapp.utils.Location;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class GUIForecastComposite extends JPanel implements Updateable{
    public abstract void addListener(ActionListener listener);
}
