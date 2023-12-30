package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.model.FoodItem;
import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.control.ConsumableManager;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import ca.cmpt213.a4.client.model.DrinkItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This Class is the Graphic ui of the program. This is where the user
 * interacts with the program and inputs and recieves information. All the
 * elements are graphic for easy use
 * @author Rio Samson
 */
public class GraphicUI implements ActionListener, WindowListener {
    private ConsumableManager manager;
    private JFrame mainJFrame;
    private JPanel mainPanel;
    private JPanel pageButtonPanel;
    private JButton allPageButton;
    private JButton expiredPageButton;
    private JButton notExpiredPageButton;
    private JButton expire7dayPageButton;
    private JButton addItemButton;
    private JScrollPane allFoodScrollPane;
    private ConsumableFactory factory;

    private final String APP_NAME = "Consumables Tracker";
    private final String ALL_BTN = "All";
    private final String EXP_BTN = "Expired";
    private final String NON_EXP_BTN = "Not Expired";
    private final String EXP_7_BTN = "Expiring in 7 Days";
    private final String ADD_ITEM_STR = "Add Item";
    private final String EMPTY_ITEMS = "No items to show.";
    private final String EMPTY_EXP_ITEMS = "No expired items to show.";
    private final String EMPTY_NON_EXP_ITEMS = "No non-expired items to show.";
    private final String EMPTY_7EXP_ITEMS = "No items expiring in 7 days to show.";
    private final String DELETE_STR = "Remove ";

    /**
     * Calls all the helper methods to initialize the GUI. Loads the manager
     * @author Rio Samson
     */
    public GraphicUI() {
        manager = new ConsumableManager();
        factory = new ConsumableFactory();
        mainJFrame = new JFrame(APP_NAME);
        mainJFrame.setSize(600, 600);
        mainJFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainJFrame.addWindowListener(this);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        setupTopPanelOfButtons();
        setupScrollPaneListOfItems();
        setupAddItemButton();

        populateJScrollWithAll(manager.allList());

        mainJFrame.add(mainPanel);
        mainJFrame.setVisible(true);
    }

    /**
     * This helper method sets up the top set of buttons that are
     * used to change between different pages of items listed.
     * @author Rio Samson
     */
    private void setupTopPanelOfButtons() {
        pageButtonPanel = new JPanel();
        pageButtonPanel.setLayout(new BoxLayout(pageButtonPanel, BoxLayout.X_AXIS));
        pageButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        allPageButton = new JButton(ALL_BTN);
        allPageButton.addActionListener(this);
        pageButtonPanel.add(allPageButton);
        expiredPageButton = new JButton(EXP_BTN);
        expiredPageButton.addActionListener(this);
        pageButtonPanel.add(expiredPageButton);
        notExpiredPageButton = new JButton(NON_EXP_BTN);
        notExpiredPageButton.addActionListener(this);
        pageButtonPanel.add(notExpiredPageButton);
        expire7dayPageButton = new JButton(EXP_7_BTN);
        expire7dayPageButton.addActionListener(this);
        pageButtonPanel.add(expire7dayPageButton);
        mainPanel.add(pageButtonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(50, 50)));
    }

    /**
     *This helper method sets up the scroll panel where all the different
     * set of items are listed.
     * @author Rio Samson
     */
    private void setupScrollPaneListOfItems() {
        allFoodScrollPane = new JScrollPane();
        allFoodScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(allFoodScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 40)));
    }

    /**
     *This method sets up the add consumable item button at the bottom
     * of the screen. Makes it so that it pops up the dialog.
     * @author Rio Samson
     */
    private void setupAddItemButton() {
        addItemButton = new JButton(ADD_ITEM_STR);
        addItemButton.addActionListener(this);
        addItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemButton.setPreferredSize(new Dimension(100, 50));
        mainPanel.add(addItemButton);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 40)));
    }

    /**
     *This method is called to populate the scrollPane with all
     * of the food items. Access from the manager, and prints every item.
     * Makes panels for each item and a button for removing the item.
     * @author Rio Samson
     */
    private void populateJScrollWithAll(ArrayList<Consumable> consumablesList) {
        JPanel panelOfItems = new JPanel();
        panelOfItems.setLayout(new BoxLayout(panelOfItems, BoxLayout.Y_AXIS));
        if (consumablesList.isEmpty()) {
            JPanel itemPanel = makeItemPanel(EMPTY_ITEMS, 0, null);
            panelOfItems.add(itemPanel);
            panelOfItems.add(Box.createRigidArea(new Dimension(10, 10)));
        } else {
            for (int i = 0; i < consumablesList.size(); i++) {
                JPanel itemPanel = makeItemPanel(consumablesList.get(i).toString(), i + 1,
                        consumablesList.get(i));
                JButton removeButton = new JButton(DELETE_STR + (i + 1));
                removeButton.addActionListener(this);
                itemPanel.add(removeButton);
                final int index = (i);
                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        populateJScrollWithAll(manager.delete(index));
                    }
                });
                panelOfItems.add(itemPanel);
                panelOfItems.add(Box.createRigidArea(new Dimension(10, 10)));
            }
        }
        allFoodScrollPane.setViewportView(panelOfItems);
    }

    /**
     *This method populates every other set of items like expired, non expired etc.
     * Makes individual panels for each item, then adds it to the scroll pane.
     * @author Rio Samson
     */
    private void populateJScroll(ArrayList<Consumable> consumablesList, String emptyMessage) {
        JPanel panelOfItems = new JPanel();
        panelOfItems.setLayout(new BoxLayout(panelOfItems, BoxLayout.Y_AXIS));
        if (consumablesList.isEmpty()) {
            JPanel itemPanel = makeItemPanel(emptyMessage, 0, null);
            panelOfItems.add(itemPanel);
            panelOfItems.add(Box.createRigidArea(new Dimension(10, 10)));
        } else {
            for (int i = 0; i < consumablesList.size(); i++) {
                JPanel itemPanel = makeItemPanel(consumablesList.get(i).toString(), i + 1,
                        consumablesList.get(i));
                panelOfItems.add(itemPanel);
                panelOfItems.add(Box.createRigidArea(new Dimension(10, 10)));
            }
        }
        allFoodScrollPane.setViewportView(panelOfItems);
    }

    /**
     *This method is called by each of the populate panel methods.
     * Makes the individual panels for the items correctly and returns those panels.
     * Makes border for the panels.
     * @author Rio Samson
     */
    private JPanel makeItemPanel(String info, int index, Consumable item) {
        String itemName;
        if (item == null) {
            itemName = "";
        } else if (item instanceof FoodItem) {
            itemName = "(Food)";
        } else if (item instanceof DrinkItem) {
            itemName = "(Drink)";
        } else {
            itemName = "";
        }
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JTextArea text = new JTextArea(info);
        String name = "Item #" + index + " " + itemName;
        Border border = BorderFactory.createTitledBorder(name);
        panel.setBorder(border);
        panel.add(text);
        return panel;
    }

    /**
     *Override function for each of the onclick listeners for any items.
     * Listens for the all the buttons in the screen
     * @author Rio Samson
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ADD_ITEM_STR)){
            AddItemDialog dialog = new AddItemDialog(new JFrame(), ADD_ITEM_STR, manager);
            populateJScrollWithAll(manager.allList());
        } else if (e.getActionCommand().equals(ALL_BTN)) {
            populateJScrollWithAll(manager.allList());
        } else if (e.getActionCommand().equals(EXP_BTN)) {
            populateJScroll(manager.expiredList(), EMPTY_EXP_ITEMS);
        } else if (e.getActionCommand().equals(NON_EXP_BTN)) {
            populateJScroll(manager.nonExpiredList(), EMPTY_NON_EXP_ITEMS);
        } else if (e.getActionCommand().equals(EXP_7_BTN)) {
            populateJScroll(manager.expireInWeek(), EMPTY_7EXP_ITEMS);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     *This override method called while closing to save the data
     * @author Rio Samson
     */
    @Override
    public void windowClosing(WindowEvent e) {
        Process process;
        String command = "curl -X GET localhost:8080/exit";
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException f) {
            f.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
