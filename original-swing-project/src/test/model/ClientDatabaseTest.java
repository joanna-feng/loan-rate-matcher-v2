package model;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ClientDatabaseTest {
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientDatabase database;

    @BeforeEach
    public void setUp() {
        client1 = new Client(1, "Alice", 750);
        client2 = new Client(2, "Bob", 620);
        client3 = new Client(3, "Alice", 580); // Duplicate name test case
        database = new ClientDatabase();
    }

    @Test
    public void testAddClient() {
        assertTrue(database.addClient(client1));
        assertTrue(database.addClient(client2));
        List<Client> clients = database.viewAllClients();
        assertEquals(2, clients.size());
        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
    }

    @Test
    public void testAddClientWithDuplicateName() {
        assertTrue(database.addClient(client1));
        assertFalse(database.addClient(client3));
        List<Client> clients = database.viewAllClients();
        assertEquals(1, clients.size());
        assertFalse(clients.contains(client3));
    }

    @Test
    public void testUpdateClientCreditScore() {
        database.addClient(client1);
        assertTrue(database.updateClient(1, 780));
        assertEquals(780, database.viewAllClients().get(0).getCreditScore());
    }

    @Test
    public void testUpdateClientCreditScoreWithMultipleClients() {
        database.addClient(client1);
        database.addClient(client2);
        assertTrue(database.updateClient(1, 800));
        assertEquals(800, database.viewAllClients().get(0).getCreditScore());
        assertEquals(620, database.viewAllClients().get(1).getCreditScore());
    }

    @Test
    public void testUpdateLastClientInList() {
        database.addClient(client1);
        database.addClient(client2);
        Client client4 = new Client(4, "David", 600);
        database.addClient(client4);
        assertTrue(database.updateClient(4, 750));
        assertEquals(750, database.viewAllClients().get(2).getCreditScore());
    }

    @Test
    public void testUpdateNonExistingClient() {
        assertFalse(database.updateClient(99, 800));
        assertEquals(0, database.viewAllClients().size());
    }

    @Test
    public void testDeleteClient() {
        database.addClient(client1);
        assertTrue(database.deleteClient(1));
        assertEquals(0, database.viewAllClients().size());
    }

    @Test
    public void testDeleteNonExistingClient() {
        assertFalse(database.deleteClient(99));
    }

    @Test
    public void testViewAllClientsOnEmptyDatabase() {
        assertTrue(database.viewAllClients().isEmpty());
    }

    @Test
    public void testAddAndDeleteAllClients() {
        database.addClient(client1);
        database.addClient(client2);
        database.deleteClient(1);
        database.deleteClient(2);
        assertTrue(database.viewAllClients().isEmpty());
    }

    /*
     * EFFECTS: Tests converting the database to a JSON object.
     */
    @Test
    public void testToJson() {
        database.addClient(client1);
        database.addClient(client2);

        JSONObject json = database.toJson();
        assertTrue(json.has("clients"));

        JSONArray jsonArray = json.getJSONArray("clients");
        assertEquals(2, jsonArray.length());

        JSONObject jsonClient1 = jsonArray.getJSONObject(0);
        assertEquals(1, jsonClient1.getInt("id"));
        assertEquals("Alice", jsonClient1.getString("name"));
        assertEquals(750, jsonClient1.getInt("creditScore"));

        JSONObject jsonClient2 = jsonArray.getJSONObject(1);
        assertEquals(2, jsonClient2.getInt("id"));
        assertEquals("Bob", jsonClient2.getString("name"));
        assertEquals(620, jsonClient2.getInt("creditScore"));
    }

    @Test
    void testLoadEmptyDatabase() {
        JSONObject json = new JSONObject();
        json.put("clients", new JSONArray());

        database.loadFromJson(json);
        assertTrue(database.viewAllClients().isEmpty());
    }

    @Test
    void testLoadMultipleClients() {
        JSONObject json = new JSONObject();
        json.put("clients", new JSONArray(List.of(
                new JSONObject().put("id", 1).put("name", "Alice").put("creditScore", 750),
                new JSONObject().put("id", 2).put("name", "Bob").put("creditScore", 620))));

        database.loadFromJson(json);
        List<Client> clients = database.viewAllClients();

        assertEquals(2, clients.size());
        assertEquals(1, clients.get(0).getId());
        assertEquals("Alice", clients.get(0).getName());
        assertEquals(750, clients.get(0).getCreditScore());

        assertEquals(2, clients.get(1).getId());
        assertEquals("Bob", clients.get(1).getName());
        assertEquals(620, clients.get(1).getCreditScore());
    }

    @Test
    void testLoadDatabaseWithDuplicateClients() {
        JSONObject json = new JSONObject();
        json.put("clients", new JSONArray(List.of(
                new JSONObject().put("id", 1).put("name", "Alice").put("creditScore", 750),
                new JSONObject().put("id", 1).put("name", "Alice Dup").put("creditScore", 600))));

        database.loadFromJson(json);
        List<Client> clients = database.viewAllClients();

        assertEquals(2, clients.size());
    }

    @Test
    public void testFilterByCreditScore() {
        database.addClient(client1); 
        database.addClient(client2); 
        database.addClient(new Client(4, "Charlie", 580));
        List<Client> filtered = database.filterByCreditScore(700);
        assertEquals(1, filtered.size());
        assertEquals("Alice", filtered.get(0).getName());
    }

    @Test
    public void testFilterByCreditScoreNoneMatch() {
        database.addClient(client2); 
        database.addClient(new Client(4, "Charlie", 580));
        List<Client> filtered = database.filterByCreditScore(800);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testFilterByCreditScoreAllMatch() {
        database.addClient(client1); 
        database.addClient(new Client(4, "Charlie", 710));
        List<Client> filtered = database.filterByCreditScore(700);
        assertEquals(2, filtered.size());
    }

    @Test
    public void testPrintEventLog() {
        database.addClient(client1);
        database.updateClient(1, 800);
        database.filterByCreditScore(700);
        database.deleteClient(1);
        database.printEventLog(); 
    }
}
