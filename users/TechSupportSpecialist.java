package users;

import enums.RequestStatus;
import java.util.List;
import storage.Database;
import system.Request;

public class TechSupportSpecialist extends Employee {
    private static final long serialVersionUID = 1L;

    public TechSupportSpecialist(String id, String username, String password, String fullName,
                                 String email, String department) {
        super(id, username, password, fullName, email, department);
    }

    public List<Request> seeNewRequests(Database database) {
        List<Request> requests = database.getRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW)
                .toList();
        requests.forEach(Request::view);
        return requests;
    }

    public void accept(Request request) {
        request.setStatus(RequestStatus.ACCEPTED);
    }

    public void reject(Request request) {
        request.setStatus(RequestStatus.REJECTED);
    }

    public void done(Request request) {
        request.setStatus(RequestStatus.DONE);
    }
}
