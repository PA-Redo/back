package fr.croixrouge.exposition.dto;

import fr.croixrouge.domain.model.ID;

import java.time.LocalDateTime;

public class MessageDto {
    private final long author;
    private final String content;
    private final LocalDateTime date;


    public MessageDto(long author, String content, LocalDateTime date) {
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
}
