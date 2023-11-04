package fr.croixrouge.repository.db.chat;

import fr.croixrouge.repository.db.user.UserDB;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "message")
@Entity
public class MessageDB {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_db_id", nullable = false)
    private ChatDB chatDB;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_db_id", nullable = false)
    private UserDB userDB;

    public MessageDB(Long id, ChatDB chatDB, String message, LocalDateTime date, UserDB userDB) {
        this.id = id;
        this.chatDB = chatDB;
        this.message = message;
        this.date = date;
        this.userDB = userDB;
    }

    public Long getId() {
        return id;
    }

    public ChatDB getChatDB() {
        return chatDB;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UserDB getUserDB() {
        return userDB;
    }
}
