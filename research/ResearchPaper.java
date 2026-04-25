package research;

import enums.Format;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final List<Researcher> authors = new ArrayList<>();
    private final Journal journal;
    private final int pages;
    private final LocalDate publishedDate;
    private final String doi;
    private int citations;

    public ResearchPaper(String title, Journal journal, int pages, LocalDate publishedDate, String doi, int citations) {
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.publishedDate = publishedDate;
        this.doi = doi;
        this.citations = citations;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getCitations() {
        return citations;
    }

    public void addAuthor(Researcher researcher) {
        if (!authors.contains(researcher)) {
            authors.add(researcher);
        }
    }

    public String getCitation(Format format) {
        String authorText = authors.stream().map(a -> a.getOwner().getFullName()).reduce((a, b) -> a + ", " + b).orElse("Unknown");
        if (format == Format.BIBTEX) {
            return "@article{" + doi.replace("/", "_") + ", title={" + title + "}, author={" + authorText
                    + "}, journal={" + journal.getName() + "}, year={" + publishedDate.getYear() + "}, doi={" + doi + "}}";
        }
        return authorText + ". " + title + ". " + journal.getName() + ", " + publishedDate.getYear() + ". doi:" + doi;
    }

    public static Comparator<ResearchPaper> byDate() {
        return Comparator.comparing(ResearchPaper::getPublishedDate).reversed();
    }

    public static Comparator<ResearchPaper> byCitations() {
        return Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    }

    public static Comparator<ResearchPaper> byLength() {
        return Comparator.comparingInt(ResearchPaper::getPages).reversed();
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() {
        return title + " (" + publishedDate + ", citations=" + citations + ", pages=" + pages + ")";
    }
}
