package main.tq215.weatherapp.Front;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class GUIForecastComposite extends JPanel implements Updateable{
    // an abstract, updateable class, for use in BigForecast
    public abstract void addListener(ActionListener listener);
}
