package storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {
    private static DataManager instance;
    private final String path;

    private DataManager(String path) {
        this.path = path;
    }

    public static DataManager getInstance(String path) {
        if (instance == null) {
            instance = new DataManager(path);
        }
        return instance;
    }

    public void save(Database database) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(database);
        }
    }

    public Database load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return (Database) in.readObject();
        }
    }
}
