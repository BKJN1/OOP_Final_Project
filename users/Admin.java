package users;

import java.util.List;
import storage.Database;
import system.Log;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String username, String password, String fullName, String email) {
        super(id, username, password, fullName, email, "Administration");
    }

    public void addUser(Database database, User user) {
        database.addUser(user);
        database.addLog(new Log(this, "Added user " + user.getUsername()));
    }

    public void removeUser(Database database, String userId) {
        database.removeUser(userId);
        database.addLog(new Log(this, "Removed user " + userId));
    }

    public void updateUser(Database database, User user, String username, String password, String fullName, String email) {
        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }
        if (fullName != null && !fullName.isBlank()) {
            user.setFullName(fullName);
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        database.addLog(new Log(this, "Updated user " + user.getId()));
    }

    public List<Log> viewLogs(Database database) {
        return database.getLogs();
    }
}
