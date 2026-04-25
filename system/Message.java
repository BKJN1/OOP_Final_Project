package system;

import java.io.Serializable;
import java.time.LocalDateTime;
import users.Employee;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Employee sender;
    private final Employee receiver;
    private final String text;
    private final LocalDateTime sentAt = LocalDateTime.now();

    public Message(Employee sender, Employee receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public Notification toNotification() {
        return new Notification("Message from " + sender.getFullName() + ": " + text);
    }

    @Override
    public String toString() {
        return sentAt + " " + sender.getFullName() + " -> " + receiver.getFullName() + ": " + text;
    }
}
