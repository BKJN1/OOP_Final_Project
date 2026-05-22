package interfaces;

import research.Researcher;
import users.User;

public interface CanResearch {
    Researcher getResearchProfile();

    default void setResearchProfile(Researcher researchProfile) {
        throw new UnsupportedOperationException("Research profile cannot be changed.");
    }

    default boolean isResearcher() {
        return getResearchProfile() != null;
    }

    default void becomeResearcher(User owner) {
        if (!isResearcher()) {
            setResearchProfile(new Researcher(owner));
        }
    }
}
