package fr.croixrouge.repository.db.chat;

import fr.croixrouge.repository.db.beneficiary.BeneficiaryDB;
import jakarta.persistence.*;

@Table(name = "chat")
@Entity
public class ChatDB {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "beneficiary_db_id")
    private BeneficiaryDB beneficiaryDB;

    @Column(name = "convname", nullable = false)
    private String convname;

    public ChatDB(Long id, BeneficiaryDB beneficiaryDB, String convname) {
        this.id = id;
        this.beneficiaryDB = beneficiaryDB;
        this.convname = convname;
    }

    public ChatDB() {

    }

    public Long getId() {
        return id;
    }

    public BeneficiaryDB getBeneficiaryDB() {
        return beneficiaryDB;
    }

    public String getConvname() {
        return convname;
    }

}
