package tq215.weatherapp.Front;

import main.tq215.weatherapp.Front.ForecastRow;
import main.tq215.weatherapp.Front.TwelveHour;
import main.tq215.weatherapp.utils.ForecastAtTime;
import main.tq215.weatherapp.utils.ForecastComposite;
import main.tq215.weatherapp.utils.Location;

import java.time.LocalDateTime;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TwelveHourTest {
    private static TwelveHour pane;
    public TwelveHourTest() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Location testLocation = new Location("Test", 0.0, 0.0);
        pane = new TwelveHour(testLocation);
        frame.add(pane);
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TwelveHourTest();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ForecastAtTime newForecast = new ForecastAtTime(10.0, 10.0, 10.0, 10.0, 10.0, LocalDateTime.now(), true);
        List<ForecastAtTime> forecasts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            forecasts.add(newForecast);
        }
        Location newLocation = new Location("Test", 1.0, 1.0);
        newLocation.setTwelveHour(new ForecastComposite(forecasts));
        pane.update(newLocation);
    }
}
