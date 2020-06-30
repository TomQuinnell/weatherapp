package tq215.weatherapp.Back;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import main.tq215.weatherapp.Back.Backend;
import main.tq215.weatherapp.utils.Location;

import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class forecastTest {
    @Test
    public void findAttributesOnStart() {
        Location l = new Location("TestPlace", 0.0,0.0);
        System.out.println(l.getCurrentForecast());
    }
}
