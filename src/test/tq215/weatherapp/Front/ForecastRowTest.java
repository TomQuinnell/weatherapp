package tq215.weatherapp.Front;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.Front.BigForecast;
import main.tq215.weatherapp.Front.ForecastRow;
import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.Location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class ForecastRowTest {
    private static ForecastRow pane;
    private static ForecastAtTime forecast;
    public ForecastRowTest() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Location testLocation = new Location("Test", 0.0, 0.0);
        forecast = new ForecastAtTime(1.0, 2.0, 3.0, 4.0, 5.0, LocalDateTime.now(), true);
        pane = new ForecastRow(forecast, true);
        frame.add(pane);
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ForecastRowTest();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ForecastAtTime newForecast = new ForecastAtTime(10.0, 10.0, 10.0, 10.0, 10.0, LocalDateTime.now(), true);
        pane.update(newForecast);
    }


}
