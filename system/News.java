package system;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import users.User;

public class News implements Serializable, Comparable<News> {
    private static final long serialVersionUID = 1L;

    private final String topic;
    private final String content;
    private final boolean pinned;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final List<Comment> comments = new ArrayList<>();

    public News(String topic, String content) {
        this.topic = topic;
        this.content = content;
        this.pinned = "Research".equalsIgnoreCase(topic);
    }

    public String getTopic() {
        return topic;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void addComment(User author, String text) {
        comments.add(new Comment(author, text));
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    @Override
    public int compareTo(News other) {
        if (pinned != other.pinned) {
            return pinned ? -1 : 1;
        }
        return other.createdAt.compareTo(createdAt);
    }

    @Override
    public String toString() {
        return (pinned ? "[PINNED] " : "") + topic + ": " + content + " comments=" + comments.size();
    }
}
