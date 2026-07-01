package persistence;

import model.Client;
import model.ClientDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
 
/*
 * Unit tests for the JsonWriter class.
 */
class JsonWriterTest extends JsonTest {
    private static final String EMPTY_FILE = "./data/testWriterEmptyClientDatabase.json";
    private static final String GENERAL_FILE = "./data/testWriterGeneralClientDatabase.json";
    private ClientDatabase database;
    private JsonWriter writer;
    private JsonReader reader;

    /*
     * MODIFIES: this
     * EFFECTS: Sets up a new ClientDatabase before each test.
     */
    @BeforeEach
    void setUp() {
        database = new ClientDatabase();
    }

    /*
     * EFFECTS: Tests writing an empty client database to a JSON file.
     */
    @Test
    void testWriterEmptyDatabase() {
        writer = new JsonWriter(EMPTY_FILE);
        reader = new JsonReader(EMPTY_FILE);

        try {
            writer.open();
            writer.write(database);
            writer.close();

            // Read it back and verify it's correctly saved
            database = reader.read();
            assertTrue(database.viewAllClients().isEmpty());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    /*
     * EFFECTS: Tests writing and reading a populated client database.
     */
    @Test
    void testWriterGeneralDatabase() {
        writer = new JsonWriter(GENERAL_FILE);
        reader = new JsonReader(GENERAL_FILE);

        database.addClient(new Client(3, "Alice", 750));
        database.addClient(new Client(4, "Bob", 620));

        try {
            writer.open();
            writer.write(database);
            writer.close();

            // Read it back and verify it's correctly saved
            database = reader.read();
            assertEquals(2, database.viewAllClients().size());
            checkClient(3, "Alice", 750, database.viewAllClients().get(0));
            checkClient(4, "Bob", 620, database.viewAllClients().get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}

// tests are adapted from the sample workroom reader code
