package fr.croixrouge.repository.db.chat;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.FamilyMember;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.repository.db.beneficiary.BeneficiaryDB;
import fr.croixrouge.repository.db.beneficiary.FamilyMemberDBRepository;
import fr.croixrouge.repository.db.user.InDBUserRepository;
import fr.croixrouge.storage.model.Chat;
import fr.croixrouge.storage.model.Message;
import fr.croixrouge.storage.repository.ChatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class InDBChatRepository implements ChatRepository {
    private final ChatDBRepository chatRepository;
    private final MessageDBRepository messageRepository;
    private final InDBUserRepository inDBUserRepository;
    private final FamilyMemberDBRepository familyMemberDBRepository;

    public InDBChatRepository(ChatDBRepository chatRepository, MessageDBRepository messageRepository, InDBUserRepository inDBUserRepository, FamilyMemberDBRepository familyMemberDBRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.inDBUserRepository = inDBUserRepository;
        this.familyMemberDBRepository = familyMemberDBRepository;
    }

    @Override
    public Optional<Chat> findById(ID id) {
        return chatRepository.findById(id.value()).map(this::toChat);
    }

    private Chat toChat(ChatDB chatDB) {
        return new Chat(
                ID.of(chatDB.getId()),
                toBeneficiary(chatDB.getBeneficiaryDB()),
                chatDB.getConvname(),
                messageRepository.findByChatDB_Id(ID.of(chatDB.getId()).value()).stream().map(this::toMessage).toList()
        );
    }

    private Message toMessage(MessageDB messageDB) {
        return new Message(
                ID.of(messageDB.getId()),
                ID.of(messageDB.getChatDB().getId()),
                ID.of(messageDB.getUserDB().getUserID()),
                messageDB.getMessage(),
                messageDB.getDate()
        );
    }

    public Beneficiary toBeneficiary(BeneficiaryDB beneficiaryDB) {
        return new Beneficiary(
                new ID(beneficiaryDB.getId()),
                inDBUserRepository.toUser(beneficiaryDB.getUserDB()),
                beneficiaryDB.getFirstname(),
                beneficiaryDB.getLastname(),
                beneficiaryDB.getPhonenumber(),
                beneficiaryDB.getValidated(),
                beneficiaryDB.getBirthdate(),
                beneficiaryDB.getSocialWorkerNumber(),
                familyMemberDBRepository.findByBeneficiaryDB_Id(beneficiaryDB.getId()).stream()
                        .map(familyMemberDB -> new FamilyMember(new ID(familyMemberDB.getId()),
                                familyMemberDB.getFirstname(),
                                familyMemberDB.getLastname(),
                                familyMemberDB.getBirthdate()))
                        .toList(),
                beneficiaryDB.getSolde());
    }

    @Override
    public ID save(Chat object) {
        var id = new ID(chatRepository.save(toChatDB(object)).getId());
        object.setId(id);
        return id;
    }

    private ChatDB toChatDB(Chat object) {
        return new ChatDB(
                object.getId() == null ? null : object.getId().value(),
                toBenefDB(object.getBeneficiary()),
                object.getName()
        );
    }

    private BeneficiaryDB toBenefDB(Beneficiary beneficiary) {
        return new BeneficiaryDB(beneficiary.getId() == null ? null : beneficiary.getId().value(),
                beneficiary.getFirstName(),
                beneficiary.getLastName(),
                beneficiary.getPhoneNumber(),
                inDBUserRepository.toUserDB(beneficiary.getUser()),
                beneficiary.isValidated(),
                beneficiary.getBirthDate(),
                beneficiary.getSocialWorkerNumber(),
                beneficiary.getSolde()
        );
    }

    @Override
    public void delete(Chat object) {
        chatRepository.delete(toChatDB(object));
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll().stream()
                .map(this::toChat)
                .toList();
    }

    @Override
    public void postMessage(ID conversationId, ID author, String message, LocalDateTime date) {
        messageRepository.save(
                new MessageDB(null,
                        chatRepository.findById(conversationId.value()).orElseThrow(),
                        message,
                        date,
                        inDBUserRepository.toUserDB(inDBUserRepository.findById(author).orElseThrow())
                )
        );
    }

    @Override
    public void createChat(Beneficiary author, String convname) {
        chatRepository.save(
                new ChatDB(null,
                        toBenefDB(author),
                        convname
                )
        );
    }

    @Override
    public List<Chat> findByBeneficiaryId(ID beneficiaryId) {
        return chatRepository.findByBeneficiaryDB_Id(beneficiaryId.value()).stream()
                .map(this::toChat)
                .toList();
    }

    @Override
    public List<Message> findMessagesByChatId(ID chatId) {
        return messageRepository.findByChatDB_Id(chatId.value()).stream()
                .map(this::toMessage)
                .toList();
    }

    @Override
    public Optional<Chat> findChatByChatId(ID chatId) {
        return chatRepository.findById(chatId.value()).map(this::toChat);
    }
}
