package users;

import interfaces.CanResearch;
import research.Researcher;

public class ResearchEmployee extends Employee implements CanResearch {
    private static final long serialVersionUID = 1L;

    private final Researcher researchProfile;

    public ResearchEmployee(String id, String username, String password, String fullName,
                            String email, String department) {
        super(id, username, password, fullName, email, department);
        this.researchProfile = new Researcher(this);
    }

    @Override
    public Researcher getResearchProfile() {
        return researchProfile;
    }
}
