package tq215.weatherapp.Front;

import main.tq215.weatherapp.Front.BigForecast;
import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.utils.Location;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class BigForecastTest {
    public BigForecastTest() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Location testLocation = new Location("Test", 0.0, 0.0);
        frame.add(new BigForecast(testLocation));
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BigForecastTest();
    }


}
