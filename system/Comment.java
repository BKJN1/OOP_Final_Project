package system;

import java.io.Serializable;
import java.time.LocalDateTime;
import users.User;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final User author;
    private final String text;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Comment(User author, String text) {
        this.author = author;
        this.text = text;
    }

    @Override
    public String toString() {
        return author.getFullName() + " (" + createdAt + "): " + text;
    }
}
