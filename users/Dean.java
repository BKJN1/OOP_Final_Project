package users;

import system.Request;

public class Dean extends Employee {
    private static final long serialVersionUID = 1L;

    public Dean(String id, String username, String password, String fullName, String email, String department) {
        super(id, username, password, fullName, email, department);
    }

    public void signRequest(Request request) {
        request.sign(this);
    }
}
