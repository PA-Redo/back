package fr.croixrouge.exposition.dto;

import java.util.List;

public class ChatDto {
    private final String conversationId;
    private final List<MessageDto> messages;
    private final String convname;


    public ChatDto(String conversationId, List<MessageDto> messages, String convname) {
        this.conversationId = conversationId;
        this.messages = messages;
        this.convname = convname;
    }

    public String getConvname() {
        return convname;
    }
}
