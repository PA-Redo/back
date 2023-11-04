package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.Chat;
import fr.croixrouge.storage.model.Message;
import fr.croixrouge.storage.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final BeneficiaryService beneficiaryService;

    public ChatService(ChatRepository chatRepository, BeneficiaryService beneficiaryService) {
        this.chatRepository = chatRepository;
        this.beneficiaryService = beneficiaryService;
    }

    public List<Chat> allChatOfBeneficiary(ID beneficiaryId) {
        return chatRepository.findByBeneficiaryId(beneficiaryId);
    }

    public List<Chat> findAllChat() {
        return chatRepository.findAll();
    }

    public List<Message> allMessageOfChat(ID chatId) {
        return chatRepository.findMessagesByChatId(chatId);
    }

    public void sendMessage(ID conversationId, ID author, String message) {
        chatRepository.postMessage(conversationId, author, message);
    }

    public void createChat(ID author, String convname, String firstMessage) {
        chatRepository.createChat(beneficiaryService.findByUserId(author), convname, firstMessage);
    }
}
