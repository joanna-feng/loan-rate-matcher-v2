package ui;

import model.Client;
import model.ClientDatabase;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/*
 * Represents the graphical user interface (GUI) for the Loan Rate Matcher application.
 */
public class LoanRateMatcherGUI extends JFrame {
    private static final String JSON_STORE = "./data/clientDatabase.json";
    private ClientDatabase database;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private DefaultListModel<String> clientListModel;
    private JList<String> clientList;

    /*
     * EFFECTS: Constructs the GUI for the Loan Rate Matcher application.
     */
    public LoanRateMatcherGUI(boolean showPrompt) {
        super("Loan Rate Matcher");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        database = new ClientDatabase();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);

        add(createControlPanel(), BorderLayout.NORTH);
        add(new JScrollPane(clientList), BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center window
        setVisible(true); // Ensure UI is displayed first

        // Show load prompt **after** the UI is fully visible
        if (showPrompt) {
            SwingUtilities.invokeLater(this::showLoadPrompt);
        }
        // Calls showExitPrompt when user closes the window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                showExitPrompt();
            }
        });
    }

    /*
     * EFFECTS: Creates the control panel with buttons for user actions.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4));

        JButton addButton = new JButton("Add Client");
        addButton.addActionListener(this::handleAddClient);

        JButton filterButton = new JButton("Filter Clients");
        filterButton.addActionListener(this::handleFilterClients);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveClientDatabase());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadClientDatabase());

        panel.add(addButton);
        panel.add(filterButton);
        panel.add(saveButton);
        panel.add(loadButton);

        return panel;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Handles adding a new client and updates the list.
     */
    private void handleAddClient(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter Client Name:");
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        int id = database.viewAllClients().size() + 1;
        int creditScore;
        try {
            creditScore = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Credit Score:"));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid credit score. Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = new Client(id, name, creditScore);
        addValidClient(client);
    }

    /**
     * MODIFIES: this, database
     * EFFECTS: If the given client is successfully added to the database, adds the
     * client's details to the list model for display. The client's details include
     * ID, name, credit score, and loan rate. If a client with the same name already
     * exists, shows an error message dialog to the user indicating the duplication.
     */

    private void addValidClient(Client client) {
        if (database.addClient(client)) {
            clientListModel.addElement(
                    "ID: " + client.getId()
                            + " | Name: " + client.getName()
                            + " | Credit Score: " + client.getCreditScore()
                            + " | Loan Rate: " + client.getLoanRate() + "%");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Client with this name already exists!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * EFFECTS: Prompts the user for a minimum credit score and filters the
     * displayed clients.
     */
    private void handleFilterClients(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Enter minimum credit score:");
        if (input == null) {
            return;
        }

        try {
            int minCreditScore = Integer.parseInt(input);
            clientListModel.clear();
            for (Client client : database.filterByCreditScore(minCreditScore)) {
                clientListModel.addElement(
                        "ID: " + client.getId()
                                + " | Name: " + client.getName()
                                + " | Credit Score: " + client.getCreditScore()
                                + " | Loan Rate: " + client.getLoanRate() + "%");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input. Please enter a valid number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * EFFECTS: Saves the current client database to a JSON file.
     */
    private void saveClientDatabase() {
        try {
            jsonWriter.open();
            jsonWriter.write(database);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Database saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Could not save data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: Loads the client database from a JSON file and updates the list.
     */
    private void loadClientDatabase() {
        try {
            database = jsonReader.read();
            clientListModel.clear();
            for (Client client : database.viewAllClients()) {
                clientListModel.addElement(
                        "ID: " + client.getId()
                                + " | Name: " + client.getName()
                                + " | Credit Score: " + client.getCreditScore()
                                + " | Loan Rate: " + client.getLoanRate() + "%");
            }
            JOptionPane.showMessageDialog(this, "Database loaded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Could not load data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * EFFECTS: Prompts the user to save data before exiting.
     * Then prints all logged events to the console.
     */
    private void showExitPrompt() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Would you like to save your data before exiting?",
                "Save Data",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            saveClientDatabase();
        }
        // Indirectly print events by calling the model
        database.printEventLog();
        System.exit(0);
    }

    /*
     * EFFECTS: Prompts the user to load data at startup.
     */
    private void showLoadPrompt() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Would you like to load saved data?",
                "Load Data",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            loadClientDatabase();
        }
    }

    /*
     * EFFECTS: Main method to start the GUI application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}