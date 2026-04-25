package users;

import exceptions.LowHIndexException;
import java.util.ArrayList;
import java.util.List;
import research.ResearchPaper;
import research.Researcher;

public class GraduateStudent extends Student {
    private static final long serialVersionUID = 1L;

    private Researcher supervisor;
    private final List<ResearchPaper> diplomaPapers = new ArrayList<>();

    public GraduateStudent(String id, String username, String password, String fullName, String email,
                           String major, int yearOfStudy) {
        super(id, username, password, fullName, email, major, yearOfStudy);
        setResearchProfile(new Researcher(this));
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public void assignSupervisor(Researcher supervisor) throws LowHIndexException {
        if (supervisor.calculateHIndex() < 3) {
            throw new LowHIndexException("Supervisor must have h-index >= 3.");
        }
        this.supervisor = supervisor;
    }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
    }

    public List<ResearchPaper> getDiplomaPapers() {
        return new ArrayList<>(diplomaPapers);
    }
}
