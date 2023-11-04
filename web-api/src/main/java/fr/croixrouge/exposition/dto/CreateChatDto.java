package fr.croixrouge.exposition.dto;

public class CreateChatDto {
    private final String convname;
    private final long author;

    public CreateChatDto(String convname, long author) {
        this.convname = convname;
        this.author = author;
    }

    public long getAuthor() {
        return author;
    }

    public String getConvname() {
        return convname;
    }
}
