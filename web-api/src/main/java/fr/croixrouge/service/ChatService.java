package fr.croixrouge.service;

import fr.croixrouge.domain.model.Beneficiary;
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

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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

    public void sendMessage(ID conversationId, ID author, String message, LocalDateTime date) {
        chatRepository.postMessage(conversationId, author, message, date);
    }

    public void createChat(Beneficiary author, String convname) {
        chatRepository.createChat(author, convname);
    }
}
