package persistence; 

import model.ClientDatabase;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
 * Represents a writer that writes JSON representation of the client database to a file.
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    /*
     * EFFECTS: Constructs a writer to write to the specified destination file.
     */
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /*
     * MODIFIES: this
     * EFFECTS: Opens the writer; throws FileNotFoundException if the destination file cannot be opened.
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    /*
     * MODIFIES: this
     * EFFECTS: Writes JSON representation of the client database to a file.
     */
    public void write(ClientDatabase database) {
        JSONObject json = database.toJson();
        writer.print(json.toString(TAB));
    }

    /*
     * MODIFIES: this
     * EFFECTS: Closes the writer.
     */
    public void close() {
        writer.close();
    }
}

// Adapted from the sample workroom reader code