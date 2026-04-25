package users;

import interfaces.CanSendMessage;
import system.Message;

public abstract class Employee extends User implements CanSendMessage {
    private static final long serialVersionUID = 1L;

    private String department;

    protected Employee(String id, String username, String password, String fullName, String email, String department) {
        super(id, username, password, fullName, email);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public Message sendMessage(Employee receiver, String text) {
        Message message = new Message(this, receiver, text);
        receiver.update(message.toNotification());
        return message;
    }
}
