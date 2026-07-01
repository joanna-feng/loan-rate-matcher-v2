package persistence;

import org.json.JSONObject;

// Represents an interface for objects that can be serialized into JSON format

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}

// Adapted from the sample workroom reader code 