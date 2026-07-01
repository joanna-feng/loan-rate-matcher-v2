package ui;

import model.Client;
import model.ClientDatabase;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//Console-based UI for the Loan Rate Matcher application.

public class LoanRateMatcherApp {
    private static final String JSON_STORE = "./data/clientDatabase.json";
    private ClientDatabase database;
    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /*
     * MODIFIES: this
     * EFFECTS: Constructs the LoanRateMatcherApp and initializes the client
     * database
     */
    public LoanRateMatcherApp() {
        database = new ClientDatabase();
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    /*
     * MODIFIES: this
     * EFFECTS: Runs the main loop of the application
     */
    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                running = handleUserChoice(running, choice);
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        System.out.println("Exiting application.");
    }

    /*
     * EFFECTS: Displays the menu options to the user.
     */
    private void displayMenu() {
        System.out.println("\nLoan Rate Matcher - Menu");
        System.out.println("1. Add Client");
        System.out.println("2. View Clients");
        System.out.println("3. Update Client Credit Score");
        System.out.println("4. Delete Client");
        System.out.println("5. Save Data");
        System.out.println("6. Load Data");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    /*
     * MODIFIES: this
     * EFFECTS: Processes user input based on menu choice.
     */
    private boolean handleUserChoice(boolean running, int choice) {
        if (choice == 1) {
            addClient();
        } else if (choice == 2) {
            viewClients();
        } else if (choice == 3) {
            updateClient();
        } else if (choice == 4) {
            deleteClient();
        } else if (choice == 5) {
            saveClientDatabase();
        } else if (choice == 6) {
            loadClientDatabase();
        } else if (choice == 7) {
            running = false;
        } else {
            printInvalidChoice();
        }
        return running;
    }

    /*
     * EFFECTS: Prints invalid choice message.
     */
    private void printInvalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    /*
     * REQUIRES: Client ID > 0, name is non-empty, creditScore >= 0
     * MODIFIES: this
     * EFFECTS: Adds a new client to the database if the name is unique.
     */
    private void addClient() {
        int id = getValidClientId();
        System.out.print("Enter Client Name: ");
        String name = scanner.nextLine();
        int creditScore = getValidCreditScore();

        Client client = new Client(id, name, creditScore);
        if (database.addClient(client)) {
            System.out.println("Client added successfully.");
        } else {
            System.out.println("Client with this name already exists.");
        }
    }

    /*
     * EFFECTS: Displays all stored clients.
     */
    private void viewClients() {
        System.out.println("\nList of Clients:");
        for (Client client : database.viewAllClients()) {
            System.out.println("ID: " + client.getId() + ", Name: " + client.getName() + ", Credit Score: "
                    + client.getCreditScore() + ", Estimated Loan Rate: " + client.getLoanRate() + "%");
        }
    }

    /*
     * REQUIRES: Client ID exists in database, newCreditScore >= 0
     * MODIFIES: this
     * EFFECTS: Updates the credit score of the specified client.
     */
    private void updateClient() {
        System.out.print("Enter Client ID to update: ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            int newCreditScore = getValidCreditScore();
            if (database.updateClient(id, newCreditScore)) {
                System.out.println("Client credit score updated successfully.");
            } else {
                System.out.println("Client ID not found.");
            }
        } else {
            System.out.println("Invalid input. Client ID must be a number.");
            scanner.nextLine();
        }
    }

    /*
     * REQUIRES: Client ID exists in database
     * MODIFIES: this
     * EFFECTS: Deletes a client from the database.
     */
    private void deleteClient() {
        System.out.print("Enter Client ID to delete: ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            if (database.deleteClient(id)) {
                System.out.println("Client deleted successfully.");
            } else {
                System.out.println("Client ID not found.");
            }
        } else {
            System.out.println("Invalid input. Client ID must be a number.");
            scanner.nextLine();
        }
    }

    /*
     * EFFECTS: Saves the current client database to a file.
     */
    private void saveClientDatabase() {
        try {
            jsonWriter.open();
            jsonWriter.write(database);
            jsonWriter.close();
            System.out.println("Client database saved successfully to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to save client database.");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: Loads the client database from a file.
     */
    private void loadClientDatabase() {
        try {
            database = jsonReader.read();
            System.out.println("Client database loaded successfully from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Error: Unable to load client database.");
        }
    }

    /*
     * EFFECTS: Prompts user to enter a valid client ID.
     */
    private int getValidClientId() {
        int id;
        while (true) {
            System.out.print("Enter Client ID: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine();
                if (id > 0) {
                    return id;
                }
                System.out.println("Invalid input. Client ID must be a positive number.");
            } else {
                System.out.println("Invalid input. Client ID must be a number.");
                scanner.nextLine();
            }
        }
    }

    /*
     * EFFECTS: Prompts user to enter a valid credit score.
     */
    private int getValidCreditScore() {
        int creditScore;
        while (true) {
            System.out.print("Enter Credit Score: ");
            if (scanner.hasNextInt()) {
                creditScore = scanner.nextInt();
                scanner.nextLine();
                if (creditScore >= 0) {
                    return creditScore;
                }
                System.out.println("Invalid input. Credit Score must be non-negative.");
            } else {
                System.out.println("Invalid input. Credit Score must be a number.");
                scanner.nextLine();
            }
        }
    }
}