package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.ChatDto;
import fr.croixrouge.exposition.dto.CreateChatDto;
import fr.croixrouge.exposition.dto.CreateMessageDto;
import fr.croixrouge.exposition.dto.MessageDto;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController extends ErrorHandler {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/messages/{conversationId}")
    public void postMessage(@PathVariable Long conversationId, @RequestBody CreateMessageDto messageDto) {
        chatService.sendMessage(ID.of(conversationId), ID.of(messageDto.getAuthor()), messageDto.getContent());
    }

    @GetMapping("/messages/{conversationId}")
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return chatService.allMessageOfChat(ID.of(conversationId)).stream()
                .map(message -> new MessageDto(message.getUsername(), message.getAuthorId().value(), message.getMessage(), message.getDate()))
                .toList();
    }

    @GetMapping("/conversations/{beneficiaryId}")
    public List<ChatDto> getConversations(@PathVariable Long beneficiaryId) {
        return chatService.allChatOfBeneficiary(ID.of(beneficiaryId)).stream()
                .map(chat -> new ChatDto(
                                chat.getId().value(),
                                chat.getName()
                        )
                ).toList();
    }

    @GetMapping("/conversations")
    public List<ChatDto> getConversations() {
        return chatService.findAllChat().stream().map(chat -> new ChatDto(
                        chat.getId().value(),
                        chat.getName()
                )
        ).toList();
    }

    @PostMapping("/conversations")
    public void createConversation(@RequestBody CreateChatDto chatDto) {
        chatService.createChat(ID.of(chatDto.getAuthor()), chatDto.getConvname(), chatDto.getFistMessage());
    }
}
