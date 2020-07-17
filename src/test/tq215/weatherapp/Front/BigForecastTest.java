package tq215.weatherapp.Front;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.Front.BigForecast;
import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.utils.Location;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class BigForecastTest {
    private static BigForecast pane;
    public BigForecastTest() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Location testLocation = new Location("Test", 0.0, 0.0);
        pane = new BigForecast(testLocation);
        frame.add(pane);
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public BigForecastTest(Location newLocation) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pane = new BigForecast(newLocation);
        frame.add(pane);
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BigForecastTest();

        double lat = 51.52;
        double lon = -0.11;
        Location london = new Location("London", lat, lon);
        Backend.getSnapshot(london);
        Backend.get12Hour(london);
        pane.update(london);
        System.out.println(london);
    }


}
