package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import users.User;

public class Researcher implements Serializable {
    private static final long serialVersionUID = 1L;

    private final User owner;
    private final List<ResearchPaper> papers = new ArrayList<>();
    private final List<ResearchProject> projects = new ArrayList<>();

    public Researcher(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
            paper.addAuthor(this);
        }
    }

    public List<ResearchPaper> getPapers() {
        return new ArrayList<>(papers);
    }

    public void addProject(ResearchProject project) {
        if (!projects.contains(project)) {
            projects.add(project);
        }
    }

    public int calculateHIndex() {
        List<Integer> citations = papers.stream()
                .map(ResearchPaper::getCitations)
                .sorted(Comparator.reverseOrder())
                .toList();
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            }
        }
        return h;
    }

    public void printPapers(Comparator<ResearchPaper> comparator) {
        papers.stream().sorted(comparator).forEach(System.out::println);
    }

    @Override
    public String toString() {
        return owner.getFullName() + " h-index=" + calculateHIndex();
    }
}
