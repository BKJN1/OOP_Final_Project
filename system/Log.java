package system;

import java.io.Serializable;
import java.time.LocalDateTime;
import users.User;

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private final User actor;
    private final String action;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Log(User actor, String action) {
        this.actor = actor;
        this.action = action;
    }

    @Override
    public String toString() {
        return createdAt + " " + actor.getUsername() + ": " + action;
    }
}
