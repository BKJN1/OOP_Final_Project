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
}
