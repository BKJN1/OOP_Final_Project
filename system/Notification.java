package system;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final LocalDateTime createdAt;

    public Notification(String message) {
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + createdAt + "] " + message;
    }
}
