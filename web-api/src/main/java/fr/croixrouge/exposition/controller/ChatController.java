package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.CreateChatDto;
import fr.croixrouge.exposition.dto.MessageDto;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.ChatService;
import org.springframework.web.bind.annotation.*;

//todo revoir les valuers de retour
@RestController
@RequestMapping("/chat")
public class ChatController extends ErrorHandler {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/messages/{conversationId}")
    public void postMessage(@PathVariable Long conversationId, @RequestBody MessageDto messageDto) {
        chatService.sendMessage(ID.of(conversationId), ID.of(messageDto.getAuthor()), messageDto.getContent(), messageDto.getDate());
    }

    @GetMapping("/messages/{conversationId}")
    public void getMessages(@PathVariable Long conversationId) {
        chatService.allMessageOfChat(ID.of(conversationId));
    }

    @GetMapping("/conversations/{beneficiaryId}")
    public void getConversations(@PathVariable Long beneficiaryId) {
        chatService.allChatOfBeneficiary(ID.of(beneficiaryId));
    }

    @GetMapping("/conversations")
    public void getConversations() {
        chatService.findAllChat();
    }

    @PostMapping("/conversations")
    public void createConversation(@RequestBody CreateChatDto chatDto) {
        chatService.createChat(ID.of(chatDto.getAuthor()), chatDto.getConvname());
    }
}
