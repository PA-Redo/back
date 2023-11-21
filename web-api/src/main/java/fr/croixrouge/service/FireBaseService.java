package fr.croixrouge.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.EventRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FireBaseService {

    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final EventRepository eventRepository;

    public FireBaseService(UserRepository userRepository, EventRepository eventRepository) throws IOException {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream()))
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, "Pa Croix Rouge");
        firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    public void updateFirebaseToken(String firebaseToken, String username) {
        userRepository.updateFirebaseToken(firebaseToken, username);
    }

    @Scheduled(cron = "0 0/15 * * * *")
    public void sendNotification() {
        eventRepository.findEventInTheNext15Minutes().stream().filter(event -> !event.getParticipants().isEmpty()).forEach(event -> {
            userRepository.findAll().stream().filter(user -> user.getFirebaseToken() != null).forEach(user -> {
                try {
                    Notification notificatdion = Notification.builder()
                            .setTitle("Event")
                            .setBody("Event is starting in 15 minutes")
                            .build();
                    firebaseMessaging.send(

                            Message.builder()
                                    .putData("title", "Event")
                                    .putData("body", "Event is starting in 15 minutes")
                                    .setWebpushConfig(
                                            WebpushConfig.builder()
                                                    .putHeader("Urgency", "high")
                                                    .build()
                                    )
                                    .setToken(user.getFirebaseToken())
                                    .setNotification(notificatdion)
                                    .build()
                    );
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void sendNotificationCheat() {
        userRepository.findAll().stream().filter(user -> user.getFirebaseToken() != null).forEach(user -> {
            try {
                Notification notificatdion = Notification.builder()
                        .setTitle("Notification")
                        .setBody("This is a notification")
                        .build();
                firebaseMessaging.send(

                        Message.builder()
                                .putData("title", "Notification")
                                .putData("body", "This is a notification")
                                .setWebpushConfig(
                                        WebpushConfig.builder()
                                                .putHeader("Urgency", "high")
                                                .build()
                                )
                                .setToken(user.getFirebaseToken())
                                .setNotification(notificatdion)
                                .build()
                );
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
