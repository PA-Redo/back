package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CRUDRepository<ID, User> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role);

    Optional<User> findByToken(String token);

    void updateFirebaseToken(String firebaseToken, String username);

}
