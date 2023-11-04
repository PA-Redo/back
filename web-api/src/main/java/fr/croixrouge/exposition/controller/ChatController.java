package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.ChatDto;
import fr.croixrouge.exposition.dto.MessageDto;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController extends ErrorHandler {

    //Chat service
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    //post messages
    @PostMapping("/messages/{conversationId}")
    public void postMessage(@PathVariable Long conversationId, @RequestBody MessageDto messageDto) {
        chatService.sendMessage(ID.of(conversationId), ID.of(messageDto.getAuthor()), messageDto.getContent(), messageDto.getDate());
    }
}
