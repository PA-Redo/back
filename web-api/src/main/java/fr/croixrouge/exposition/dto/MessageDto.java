package fr.croixrouge.exposition.dto;

public class MessageDto {
    private final String author;
    private final String content;
    private final String date;
    private final String time;


    public MessageDto(String author, String content, String date, String time) {
        this.author = author;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
