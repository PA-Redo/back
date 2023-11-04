package fr.croixrouge.exposition.dto;

import java.util.List;

public class ChatDto {
    private final long conversationId;
    private final String convname;


    public ChatDto(long conversationId, String convname) {
        this.conversationId = conversationId;
        this.convname = convname;
    }

    public String getConvname() {
        return convname;
    }
}
