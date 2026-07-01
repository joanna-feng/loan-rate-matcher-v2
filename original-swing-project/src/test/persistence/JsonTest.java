package persistence;

import model.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Provides helper methods for JSON testing.
 */
public class JsonTest {
    /*
     * EFFECTS: Compares expected and actual client attributes.
     */
    protected void checkClient(int expectedId, String expectedName, int expectedCreditScore, Client client) {
        assertEquals(expectedId, client.getId());
        assertEquals(expectedName, client.getName());
        assertEquals(expectedCreditScore, client.getCreditScore());
    }
}

// tests are adapted from the sample workroom reader code