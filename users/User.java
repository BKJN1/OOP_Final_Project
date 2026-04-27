package users;

import interfaces.Observer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import system.Notification;

public abstract class User implements Observer, Serializable, Comparable<User> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private final List<Notification> notifications = new ArrayList<>();

    protected User(String id, String username, String password, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }

    @Override
    public int compareTo(User other) {
        return fullName.compareToIgnoreCase(other.fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + id + ", " + fullName + "}";
    }
}
