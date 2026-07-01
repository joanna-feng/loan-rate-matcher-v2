package persistence;

import model.Client;
import model.ClientDatabase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the JsonReader class.
 */
class JsonReaderTest extends JsonTest {

    /*
     * EFFECTS: Tests reading from a file that does not exist.
     *          Should throw IOException.
     */
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        assertThrows(IOException.class, reader::read);
    }

    /*
     * EFFECTS: Tests reading an empty client database from a JSON file.
     */
    @Test
    void testReaderEmptyClientDatabase() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyClientDatabase.json");
        try {
            ClientDatabase db = reader.read();
            assertEquals(0, db.viewAllClients().size());
        } catch (IOException e) {
            fail("Couldn't read from file.");
        }
    }
    
    /*
     * EFFECTS: Tests reading a client database with multiple clients.
     */
    @Test
    void testReaderGeneralClientDatabase() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralClientDatabase.json");
        try {
            ClientDatabase db = reader.read();
            List<Client> clients = db.viewAllClients();

            assertEquals(2, clients.size());
            checkClient(1, "Alice", 750, clients.get(0));
            checkClient(2, "Bob", 620, clients.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file.");
        }
    }
}

// tests are adapted from the sample workroom reader code