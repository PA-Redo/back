package fr.croixrouge.exposition.dto;

import java.util.List;

public class ChatDto {
    private final String conversationId;
    private final List<MessageDto> messages;

    public ChatDto(String conversationId, List<MessageDto> messages) {
        this.conversationId = conversationId;
        this.messages = messages;
    }
}
