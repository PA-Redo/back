package fr.croixrouge.service;

import fr.croixrouge.domain.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FireBaseService {

    private final UserRepository userRepository;

    public FireBaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateFirebaseToken(String firebaseToken, String username) {
        userRepository.updateFirebaseToken(firebaseToken, username);
    }

    @Scheduled(fixedRate = 5000)
    public void test() {
        System.out.println("test");
    }
}
