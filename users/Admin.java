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

    public List<Log> viewLogs(Database database) {
        return database.getLogs();
    }
}
