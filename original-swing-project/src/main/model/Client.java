package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a client's financial profile having an id, name and credit score 
public class Client implements Writable {
    private int id; // client id
    private String name; // the client's name
    private int creditScore; // the current credit score of the client

    /*
     * REQUIRES: clientId > 0, name is not null, creditScore >= 0
     * EFFECTS: Initializes a new client profile with given attributes
     */
    public Client(int id, String name, int creditScore) {
        this.id = id;
        this.name = name;
        this.creditScore = creditScore;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    /*
     * REQUIRES: newCreditScore >= 0
     * MODIFIES: this
     * EFFECTS: Updates the credit score of the client.
     */
    public void updateCreditScore(int newCreditScore) {
        this.creditScore = newCreditScore;
        EventLog.getInstance().logEvent(new Event("Updated credit score for client ID "
                + id + " (" + name + ") to " + creditScore + "."));
    }

    /*
     * REQUIRES: creditScore >= 0
     * EFFECTS: Returns loan interest rate based on credit score tiers.
     */
    public double getLoanRate() {
        if (creditScore < 600) {
            return 10.0;
        } else if (creditScore < 700) {
            return 7.0;
        } else {
            return 4.5;
        }
    }

    /*
     * EFFECTS: Converts this client object to a JSON representation.
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("creditScore", creditScore);
        return json;
    }

}
