package model;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientTest {
    
    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client(1, "Alice", 750);
    }

    @Test
    public void testClientConstructor() {
        assertEquals(1, client.getId());
        assertEquals("Alice", client.getName());
        assertEquals(750, client.getCreditScore());
    }

    @Test
    public void testUpdateCreditScore() {
        client.updateCreditScore(780);
        assertEquals(780, client.getCreditScore());
    }

    @Test
    public void testGetLoanRate() {
        assertEquals(4.5, client.getLoanRate(), 0.01);
        client.updateCreditScore(620);
        assertEquals(7.0, client.getLoanRate(), 0.01);
        client.updateCreditScore(500);
        assertEquals(10.0, client.getLoanRate(), 0.01);
    }

    @Test
    public void testToJson() {
        JSONObject json = client.toJson();
        assertEquals(1, json.getInt("id"));
        assertEquals("Alice", json.getString("name"));
        assertEquals(750, json.getInt("creditScore"));
    }
}
 
