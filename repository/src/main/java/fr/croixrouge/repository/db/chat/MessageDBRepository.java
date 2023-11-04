package fr.croixrouge.repository.db.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageDBRepository extends CrudRepository<MessageDB, Long> {
    @Query("select m from MessageDB m where m.chatDB.id = ?1")
    List<MessageDB> findByChatDB_Id(Long id);
}
