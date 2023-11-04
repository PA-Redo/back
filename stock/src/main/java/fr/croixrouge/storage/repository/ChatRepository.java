package fr.croixrouge.storage.repository;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.storage.model.Chat;
import fr.croixrouge.storage.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends CRUDRepository<ID, Chat> {
    void postMessage(ID conversationId, ID author, String message, LocalDateTime date);

    void createChat(Beneficiary author, String convname);

    List<Chat> findByBeneficiaryId(ID beneficiaryId);

    List<Message> findMessagesByChatId(ID chatId);

    Optional<Chat> findChatByChatId(ID chatId);
}
