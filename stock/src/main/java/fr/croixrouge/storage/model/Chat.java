package fr.croixrouge.storage.model;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.model.ID;

import java.util.List;

public class Chat extends Entity<ID> {
    private final Beneficiary beneficiary;
    private final String name;
    private final List<Message> messages;
    public Chat(ID id, Beneficiary beneficiaryId, String name, List<Message> messages) {
        super(id);
        this.beneficiary = beneficiaryId;
        this.name = name;
        this.messages = messages;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public String getName() {
        return name;
    }
}
