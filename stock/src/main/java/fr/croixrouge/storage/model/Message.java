package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.time.LocalDateTime;

public class Message extends Entity<ID> {
    private ID conversationId;
    private ID authorId;
    private String message;
    private LocalDateTime date;

    public Message(ID id, ID conversationId, ID authorId, String message, LocalDateTime date) {
        super(id);
        this.conversationId = conversationId;
        this.authorId = authorId;
        this.message = message;
        this.date = date;
    }

    public ID getConversationId() {
        return conversationId;
    }

    public ID getAuthor() {
        return authorId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ID getAuthorId() {
        return authorId;
    }
}
