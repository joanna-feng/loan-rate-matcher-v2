package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Manages a collection of client profiles 

public class ClientDatabase implements Writable {
    private List<Client> clients;

    /*
     * Constructs an empty client database.
     * MODIFIES: this
     * EFFECTS: Initializes an empty list of clients.
     */
    public ClientDatabase() {
        this.clients = new ArrayList<>();
    }

    /*
     * REQUIRES: client's name is not null and doesn't exist in database
     * MODIFIES: this
     * EFFECTS: Adds the given client profile to the database if the name is not
     * already in use.
     */
    public boolean addClient(Client client) {
        for (Client existingClient : clients) {
            if (existingClient.getName().equals(client.getName())) {
                return false; // Prevent duplicate names
            }
        }
        clients.add(client);
        EventLog.getInstance().logEvent(new Event("Added client: " + client.getName()));
        return true;
    }

    /*
     * EFFECTS: Returns a list of all clients stored in the database.
     */
    public List<Client> viewAllClients() {
        return new ArrayList<>(clients);
    }

    /*
     * REQUIRES: clientId exists in the database, newCreditScore >= 0
     * MODIFIES: this
     * EFFECTS: Updates the credit score of the specified client.
     */
    public boolean updateClient(int clientId, int newCreditScore) {
        for (Client client : clients) {
            if (client.getId() == clientId) {
                client.updateCreditScore(newCreditScore);
                EventLog.getInstance().logEvent(
                        new Event("Updated credit score for client ID " + clientId + " to " + newCreditScore));
                return true; // Successfully updated
            }
        }
        return false; // Client not found
    }

    /*
     * REQUIRES: clientId exists in the database
     * MODIFIES: this
     * EFFECTS: Removes the client with the given ID from the database.
     */
    public boolean deleteClient(int clientId) {
        boolean removed = clients.removeIf(client -> client.getId() == clientId);
        if (removed) {
            EventLog.getInstance().logEvent(new Event("Deleted client with ID: " + clientId));
        }
        return removed;
    }

    /*
     * EFFECTS: Returns this client database as a JSON object.
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("clients", clientsToJson());
        return json;
    }

    /*
     * EFFECTS: Converts all clients to a JSON array and returns it.
     */
    private JSONArray clientsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Client c : clients) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Loads clients from a JSON object and updates the database.
     */
    public void loadFromJson(JSONObject jsonObject) {
        clients.clear();
        JSONArray jsonArray = jsonObject.getJSONArray("clients");
        for (Object obj : jsonArray) {
            JSONObject clientJson = (JSONObject) obj;
            clients.add(new Client(
                    clientJson.getInt("id"),
                    clientJson.getString("name"),
                    clientJson.getInt("creditScore")));
        }
        EventLog.getInstance().logEvent(new Event("Loaded client database from file."));
    }

    // EFFECTS: Returns a list of clients with credit score >= minScore
    public List<Client> filterByCreditScore(int minScore) {
        List<Client> filtered = new ArrayList<>();
        for (Client c : clients) {
            if (c.getCreditScore() >= minScore) {
                filtered.add(c);
            }
        }
        EventLog.getInstance().logEvent(
                new Event("Filtered clients with credit score >= " + minScore));
        return filtered;
    }

    // EFFECTS: Prints all events in the log to the console
    public void printEventLog() {
        EventLog log = EventLog.getInstance();
        for (Event e : log) {
            System.out.println(e.toString());
        }
    }
}
