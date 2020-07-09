package tq215.weatherapp.Front;

import main.tq215.weatherapp.Front.HomePage;
import main.tq215.weatherapp.utils.Location;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class HomePageTest {
    public HomePageTest() {
        JFrame frame = new JFrame("Test");
        frame.setSize(450,225);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<Location> mockRecents = List.of(new Location("Test0", 0.0, 0.0),
                                             new Location("Test1", 1.0, 1.0),
                                             new Location("Test2", 2.0, 2.0),
                                             new Location("Test3", 3.0, 3.0));
        Location userLoc = new Location("User lives here...", 100.0, 100.0);
        frame.add(new HomePage(mockRecents, userLoc));
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HomePageTest();
    }


}
