package services;

import storage.Database;
import system.News;

public class NewsService {
    private final Database database;

    public NewsService(Database database) {
        this.database = database;
    }

    public News publishNews(String topic, String content) {
        News news = new News(topic, content);
        database.addNews(news);
        return news;
    }

    public News announcePaper(String paperTitle) {
        return publishNews("Research", "New research paper published: " + paperTitle);
    }

    public News announceTopCitedResearcher() {
        var researcher = database.getTopCitedResearcher();
        if (researcher == null) {
            return publishNews("Research", "No researchers yet.");
        }
        return publishNews("Research", "Top cited researcher: " + researcher.getOwner().getFullName());
    }
}
