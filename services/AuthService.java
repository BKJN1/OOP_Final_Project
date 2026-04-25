package services;

import exceptions.UnauthorizedActionException;
import storage.Database;
import system.Log;
import users.User;

public class AuthService {
    private final Database database;
    private User currentUser;

    public AuthService(Database database) {
        this.database = database;
    }

    public User login(String username, String password) throws UnauthorizedActionException {
        User user = database.findByUsername(username);
        if (user == null || !user.checkPassword(password)) {
            throw new UnauthorizedActionException("Wrong username or password.");
        }
        currentUser = user;
        database.addLog(new Log(user, "Logged in"));
        return user;
    }

    public void logout() {
        if (currentUser != null) {
            database.addLog(new Log(currentUser, "Logged out"));
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
