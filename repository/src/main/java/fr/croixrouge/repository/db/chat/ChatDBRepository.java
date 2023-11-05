package fr.croixrouge.repository.db.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatDBRepository extends CrudRepository<ChatDB, Long> {
    @Query("select c from ChatDB c where c.beneficiaryDB.id = ?1")
    List<ChatDB> findByBeneficiaryDB_Id(Long id);

    @Query("select c from ChatDB c")
    List<ChatDB> findAll();
}
