package tq215.weatherapp.Front;

import main.tq215.weatherapp.Front.HomeSnapshot;
import main.tq215.weatherapp.utils.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;
import java.awt.*;

public class HomeSnapshotTest {
    public HomeSnapshotTest() {
        JFrame frame = new JFrame("Test");
        frame.setSize(450,225);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new HomeSnapshot(new Location("Test", 0.0, 0.0)));
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HomeSnapshotTest();
    }


}
