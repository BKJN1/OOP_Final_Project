package services;

import java.util.Comparator;
import java.util.List;
import research.ResearchPaper;
import research.Researcher;
import storage.Database;

public class ResearchService {
    private final Database database;

    public ResearchService(Database database) {
        this.database = database;
    }

    public List<ResearchPaper> printAllPapers(Comparator<ResearchPaper> comparator) {
        return database.getResearchers().stream()
                .flatMap(r -> r.getPapers().stream())
                .sorted(comparator)
                .toList();
    }

    public Researcher topCitedResearcher() {
        return database.getTopCitedResearcher();
    }

    public Researcher topCitedResearcherBySchool(String school) {
        return database.getResearchers().stream()
                .filter(r -> belongsToSchool(r, school))
                .max(Comparator.comparingInt(r -> r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum()))
                .orElse(null);
    }

    public Researcher topCitedResearcherOfYear(int year) {
        return database.getResearchers().stream()
                .max(Comparator.comparingInt(r -> r.getPapers().stream()
                        .filter(p -> p.getPublishedDate().getYear() == year)
                        .mapToInt(ResearchPaper::getCitations)
                        .sum()))
                .orElse(null);
    }

    private boolean belongsToSchool(Researcher researcher, String school) {
        if (researcher.getOwner() instanceof users.Employee) {
            return ((users.Employee) researcher.getOwner()).getDepartment().equalsIgnoreCase(school);
        }
        if (researcher.getOwner() instanceof users.Student) {
            return ((users.Student) researcher.getOwner()).getMajor().equalsIgnoreCase(school);
        }
        return false;
    }
}
