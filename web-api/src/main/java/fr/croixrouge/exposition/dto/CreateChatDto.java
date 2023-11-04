package fr.croixrouge.exposition.dto;

public class CreateChatDto {
    private final String convname;
    private final long author;
    private final String fistMessage;

    public CreateChatDto(String convname, long author, String fistMessage) {
        this.convname = convname;
        this.author = author;
        this.fistMessage = fistMessage;
    }

    public long getAuthor() {
        return author;
    }

    public String getConvname() {
        return convname;
    }

    public String getFistMessage() {
        return fistMessage;
    }
}
