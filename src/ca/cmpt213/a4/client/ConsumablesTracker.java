package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.GraphicUI;
import javax.swing.*;

/**
 * This is the main class where everything is called.
 * A consumable manager object is created.
 * Uses GUI for user interface.
 * @author Rio Samson
 */
public class ConsumablesTracker {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphicUI();
            }
        });
    }
}