package fr.croixrouge.exposition.dto;

import java.time.LocalDateTime;

public class CreateMessageDto {
    private final long author;
    private final String content;


    public CreateMessageDto(long author, String content) {
        this.author = author;
        this.content = content;
    }

    public Long getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
