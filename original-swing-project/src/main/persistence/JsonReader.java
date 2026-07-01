package persistence; 

import model.ClientDatabase;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/*
 * Represents a reader that reads the client database from JSON data stored in a file.
 */
public class JsonReader {
    private String source; 

    /*
     * EFFECTS: Constructs a reader to read from the specified source file.
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /*
     * EFFECTS: Reads the client database from a file and returns it.
     * Throws IOException if an error occurs while reading data from the file.
     */
    public ClientDatabase read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        ClientDatabase database = new ClientDatabase();
        database.loadFromJson(jsonObject);
        return database;
    }

    /*
     * EFFECTS: Reads the source file as a string and returns it.
     * Throws IOException if an error occurs while reading.
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }
}

// Adapted from the sample workroom reader code