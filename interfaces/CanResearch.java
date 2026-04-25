package interfaces;

import research.Researcher;

public interface CanResearch {
    Researcher getResearchProfile();

    default boolean isResearcher() {
        return getResearchProfile() != null;
    }
}
