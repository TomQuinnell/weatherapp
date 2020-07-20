package tq215.weatherapp.Front;

import main.tq215.weatherapp.Front.SearchResults;
import main.tq215.weatherapp.utils.Location;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class SearchResultsTest {
    private static SearchResults results;
    private static List<Location> locations = new ArrayList<>();
    public SearchResultsTest() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        locations.add(new Location("Cool location #1", 0.0, 0.0));
        locations.add(new Location("Cool location #2", 0.0, 0.0));
        locations.add(new Location("Cool location #3", 0.0, 0.0));
        locations.add(new Location("Cool location #4", 0.0, 0.0));
        locations.add(new Location("Cool location #5", 0.0, 0.0));
        locations.add(new Location("Cool location #6", 0.0, 0.0));
        locations.add(new Location("Cool location #7", 0.0, 0.0));
        locations.add(new Location("Cool location #8", 0.0, 0.0));
        locations.add(new Location("Cool location #9", 0.0, 0.0));
        locations.add(new Location("Cool location #10", 0.0, 0.0));

        results = new SearchResults(locations);
        frame.add(results);
        //frame.add(new JTextArea("Testerino"));
        //frame.setBackground(Color.WHITE);

        frame.pack();
        frame.setVisible(true);
    }

    private static ActionListener makeBackButtonAL() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("BACK");
            }
        };
    }

    private static ActionListener makeSearchRowAL(int i) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(results.getIthLocation(i).getName());
            }
        };
    }

    public static void main(String[] args) {
        new SearchResultsTest();

        // button stuff; add some ActionListeners
        results.addBackButtonAL(makeBackButtonAL());
        for (int i = 0; i < locations.size(); i++) {
            results.addResultActionListener(i, makeSearchRowAL(i));
        }
    }


}
