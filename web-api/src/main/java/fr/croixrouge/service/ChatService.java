package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.storage.model.Chat;
import fr.croixrouge.storage.model.Message;
import fr.croixrouge.storage.repository.ChatRepository;
import org.apache.commons.lang3.mutable.Mutable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final BeneficiaryService beneficiaryService;
    private final FireBaseService fireBaseService;

    public ChatService(ChatRepository chatRepository, BeneficiaryService beneficiaryService, FireBaseService fireBaseService) {
        this.chatRepository = chatRepository;
        this.beneficiaryService = beneficiaryService;
        this.fireBaseService = fireBaseService;
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

        List<ID> alreadySent = new ArrayList();
        alreadySent.add(author);
        getAllUsersFromChat(conversationId).stream().distinct().filter(user -> user.getId() != author).forEach(user -> {
                    if (user.getFirebaseToken() != null && !alreadySent.contains(user.getId())) {
                        System.out.println("test: " + user.getId() + " " + author);
                        System.out.println("test 2: " + user.getFirebaseToken());
                        fireBaseService.sendNotificationToUser(author, message, user.getId());
                        alreadySent.add(user.getId());
                    }
                }
        );
    }

    private List<User> getAllUsersFromChat(ID conversationId) {
        return chatRepository.findUsersByChatId(conversationId);
    }

    public void createChat(ID author, String convname, String firstMessage) {
        chatRepository.createChat(beneficiaryService.findByUserId(author), convname, firstMessage);
    }
}
