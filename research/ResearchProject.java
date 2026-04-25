package research;

import exceptions.NotResearcherException;
import interfaces.CanResearch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import users.User;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String topic;
    private final List<ResearchPaper> publishedPapers = new ArrayList<>();
    private final List<Researcher> participants = new ArrayList<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public void join(User user) throws NotResearcherException {
        if (!(user instanceof CanResearch) || !((CanResearch) user).isResearcher()) {
            throw new NotResearcherException(user.getFullName() + " is not a researcher.");
        }
        Researcher researcher = ((CanResearch) user).getResearchProfile();
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            researcher.addProject(this);
        }
    }

    public void publishPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    @Override
    public String toString() {
        return topic + " participants=" + participants.size() + " papers=" + publishedPapers.size();
    }
}
