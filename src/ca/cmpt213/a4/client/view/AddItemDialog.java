package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ConsumableManager;
import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import com.github.lgooddatepicker.components.DatePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This Class is the Graphic ui for adding an item. This is a dialog box
 * to add any information about the item. Still in GUI format. Pop up.
 * This class extends teh JDialog class as a sub
 * @author Rio Samson
 */
public class AddItemDialog extends JDialog implements ActionListener {

    private ConsumableManager manager;
    private ConsumableFactory factory = new ConsumableFactory();

    private JPanel mainPanel;
    private JButton createBtn;
    private JButton cancelBtn;
    private JTextField nameField = new JTextField();
    private JTextField notesField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField quantityField = new JTextField();
    private JComboBox foodTypeCombo;
    private JLabel quantityLabel;
    private DatePicker datePicker;

    private int itemType = 1;
    private final String CREATE_STR = "Create";
    private final String CANCEL_STR = "Cancel";
    private final String NAME_STR = "Name";
    private final String NOTES_STR = "Notes";
    private final String PRICE_STR = "Price";
    private final String WEIGHT_STR = "Weight";
    private final String VOLUME_STR = "Volume";
    private final String TYPE = "Type";
    private final String EXP_DATE = "Expiry date";
    private final String[] foodTypes= {"Food", "Drink"};

    /**
     * Calls all the helper methods to initialize teh GUI
     * @author Rio Samson
     */
    public AddItemDialog(JFrame frame, String name, ConsumableManager manager) {
        super(frame, name, ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(700, 700));
        this.manager = manager;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        setupComboBoxPanel();
        setupPanel(nameField, NAME_STR);
        setupPanel(notesField, NOTES_STR);
        setupPanel(priceField, PRICE_STR);
        setupQuantityPanel();
        setupDatePanel();
        setupCreateCancelBtnPanel();

        super.add(mainPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        super.setVisible(true);
    }

    /**
     * This method sets up the combo box used to select the item type
     * default chosen with 2 options.
     * @author Rio Samson
     */
    private void setupComboBoxPanel(){
        JPanel dropDownPanel = new JPanel();
        dropDownPanel.setLayout(new BoxLayout(dropDownPanel, BoxLayout.X_AXIS));
        dropDownPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(TYPE);
        dropDownPanel.add(nameLabel);
        dropDownPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        foodTypeCombo = new JComboBox(foodTypes);
        foodTypeCombo.setMaximumSize(new Dimension(300, foodTypeCombo.getPreferredSize().height));
        foodTypeCombo.setActionCommand(TYPE);
        foodTypeCombo.addActionListener(this);
        dropDownPanel.add(foodTypeCombo);

        mainPanel.add(dropDownPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 15)));
    }

    /**
     * This method sets up panels such as the name, notes, and price.
     * Makes a text field and a label for the name. Takes user input
     * @author Rio Samson
     */
    private void setupPanel(JTextField textField, String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        textField.setMaximumSize(new Dimension(300, textField.getPreferredSize().height));
        panel.add(textField);

        mainPanel.add(panel);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 15)));
    }

    /**
     * This method sets up the panel for the Quantity of the item.
     * has a name and a field to take input.
     * @author Rio Samson
     */
    private void setupQuantityPanel() {
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));
        setSize(new Dimension(450, 350));
        weightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quantityLabel = new JLabel(WEIGHT_STR);
        weightPanel.add(quantityLabel);
        weightPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        quantityField = new JTextField();
        quantityField.setMaximumSize(new Dimension(300, quantityField.getPreferredSize().height));
        weightPanel.add(quantityField);

        mainPanel.add(weightPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 15)));
    }

    /**
     * This method sets up the panel for the expiry date.
     * Uses a DatePicker for the date as the user input.
     * @author Rio Samson
     */
    private void setupDatePanel() {
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel(EXP_DATE);
        datePanel.add(dateLabel);
        datePanel.add(Box.createRigidArea(new Dimension(10, 10)));

        datePicker = new DatePicker();
        datePicker.setMaximumSize(new Dimension(280,
                datePicker.getPreferredSize().height)
        );
        datePanel.add(datePicker);

        mainPanel.add(datePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(15, 15)));
    }

    /**
     * This method sets up the Cancel and create buttons at the bottom.
     * Has onclick listeners
     * @author Rio Samson
     */
    private void setupCreateCancelBtnPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn = new JButton(CREATE_STR);
        createBtn.addActionListener(this);
        buttonPanel.add(createBtn);
        cancelBtn = new JButton(CANCEL_STR);
        cancelBtn.addActionListener(this);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel);
    }

    /**
     * This method is called to add an item to the server manager once
     * all input is done. Calls the https command and sends a body with the item.
     * @author Rio Samson
     */
    private void addItemToServer() {
        DecimalFormat formatter = new DecimalFormat("00");
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        String name = nameField.getText();
        String notes = notesField.getText();
        double price = Double.parseDouble(priceField.getText());
        double quantity = Double.parseDouble(quantityField.getText());
        LocalDate localDate = datePicker.getDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        LocalDateTime expiryDate = LocalDateTime.of(year, month, day, 23, 59);
        Consumable consumable = factory.getInstance(itemType, name, notes, price, expiryDate, quantity);
        Process process;
        String command = "curl -H \"Content-Type: application/json\" -X POST -d \"";
        String extension;
        if (itemType == 1) {
            extension = "addFood";
        } else {
            extension = "addDrink";
        }
        String jsonString = "{\\\"name\\\": \\\"" + name + "\\\", \\\"notes\\\": \\\"" + notes +
                "\\\" , \\\"price\\\":" + price + ", \\\"expirationDate\\\":\\\"" + year + "-" + formatter.format(month) + "-" +
                formatter.format(day) + "T23:59" + "\\\"}";
        String command2 = "\" localhost:8080/" + extension + "/" + quantity;
        String finalCommand = command + jsonString + command2;
        try {
            process = Runtime.getRuntime().exec(finalCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called to check if the user has inputed all the required fields
     * and if they are valid. Sends a error message if not.
     * @author Rio Samson
     */
    private boolean checkCorrectInputs() {
        if (nameField.getText().isEmpty()) {
            errorMessageDialog("Error: Please enter a name");
            return false;
        } else if (priceField.getText().isEmpty()) {
            errorMessageDialog("Error: Please enter a price for the new item");
            return false;
        } else if (Double.parseDouble(priceField.getText()) < 0.0) {
            errorMessageDialog("Error: Please enter a positive price");
            return false;
        } else if (quantityField.getText().isEmpty()) {
            if (itemType == 1) {
                errorMessageDialog("Error: Please enter a weight for the new item");
            } else {
                errorMessageDialog("Error: Please enter a volume for the new item");
            }
            return false;
        } else if (Double.parseDouble(quantityField.getText()) < 0.0) {
            if (itemType == 1) {
                errorMessageDialog("Error: Please enter a positive weight");
            } else {
                errorMessageDialog("Error: Please enter a positive volume");
            }
            return false;
        } else if (datePicker.getDate() == null) {
            errorMessageDialog("Error: Please enter a valid expiry date");
            return false;
        }
        return true;
    }

    /**
     * This method pops up the error message
     * @author Rio Samson
     */
    private void errorMessageDialog(String message) {
        JOptionPane.showMessageDialog(new JFrame(),
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is the listener for all the buttons etc.
     * Cancel and create buttons, or combo box
     * @author Rio Samson
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(CREATE_STR)) {
            if (checkCorrectInputs()) {
                addItemToServer();
                super.setVisible(false);
                dispose();
            }
        } else if (e.getActionCommand().equals(CANCEL_STR)) {
            super.setVisible(false);
            super.dispose();
        } else if (e.getActionCommand().equals(TYPE)) {
            itemType = foodTypeCombo.getSelectedIndex() + 1;
            if (itemType == 1) {
                quantityLabel.setText(WEIGHT_STR);
            } else {
                quantityLabel.setText(VOLUME_STR);
            }
        }
    }
}