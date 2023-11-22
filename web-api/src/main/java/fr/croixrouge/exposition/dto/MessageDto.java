package fr.croixrouge.exposition.dto;

import java.time.LocalDateTime;

public class MessageDto {
    private final String username;
    private final long author;
    private final String content;
    private final LocalDateTime date;


    public MessageDto(String username, long author, String content, LocalDateTime date) {
        this.username = username;
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public Long getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }
}
