package interfaces;

import system.Message;
import users.Employee;

public interface CanSendMessage {
    Message sendMessage(Employee receiver, String text);
}
