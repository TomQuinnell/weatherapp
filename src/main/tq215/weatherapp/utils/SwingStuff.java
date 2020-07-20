package main.tq215.weatherapp.utils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SwingStuff {
    public static JTextPane makeTextPane(String text) {
        // Function to make a JTextPane containing text
        JTextPane newPane = new JTextPane();

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_LEFT);
        newPane.setParagraphAttributes(attribs, true);
        newPane.setText(text);
        return newPane;
    }
}
