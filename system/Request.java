package system;

import enums.RequestStatus;
import enums.UrgencyLevel;
import java.io.Serializable;
import java.time.LocalDateTime;
import users.Employee;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Employee sender;
    private final String text;
    private final UrgencyLevel urgency;
    private RequestStatus status = RequestStatus.NEW;
    private Employee signedBy;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Request(Employee sender, String text, UrgencyLevel urgency) {
        this.sender = sender;
        this.text = text;
        this.urgency = urgency;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void view() {
        if (status == RequestStatus.NEW) {
            status = RequestStatus.VIEWED;
        }
    }

    public void sign(Employee signer) {
        signedBy = signer;
    }

    public Notification toNotification() {
        return new Notification("Request from " + sender.getFullName() + " [" + urgency + "]: " + text);
    }

    @Override
    public String toString() {
        return createdAt + " " + urgency + " " + status + " from " + sender.getFullName()
                + (signedBy == null ? "" : " signed by " + signedBy.getFullName()) + ": " + text;
    }
}
