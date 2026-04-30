package research;

import enums.Format;

public final class Citation {
    private Citation() {
    }

    public static String format(ResearchPaper paper, Format format) {
        return paper.getCitation(format);
    }
}
//test