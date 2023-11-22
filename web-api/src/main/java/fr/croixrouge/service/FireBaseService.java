package fr.croixrouge.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.model.Role;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.repository.EventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Profile({"prod", "fixtures-prod"})
public class FireBaseService implements IFireBaseService {

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
                                    .setAndroidConfig(
                                            AndroidConfig.builder()
                                                    .setPriority(AndroidConfig.Priority.HIGH)
                                                    .build()
                                    )
                                    .setApnsConfig(
                                            ApnsConfig.builder()
                                                    .putHeader("apns-priority", "5")
                                                    .setAps(Aps.builder().setContentAvailable(true).build())
                                                    .build()
                                    )
                                    .putData("body", "Event is starting in 15 minutes")
                                    .setWebpushConfig(
                                            WebpushConfig.builder()
                                                    .putHeader("Urgency", "high")
                                                    .putHeader("priority", "high")
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
                                .setAndroidConfig(
                                        AndroidConfig.builder()
                                                .setPriority(AndroidConfig.Priority.HIGH)
                                                .build()
                                )
                                .setApnsConfig(
                                        ApnsConfig.builder()
                                                .putHeader("apns-priority", "5")
                                                .setAps(Aps.builder().setContentAvailable(true).build())
                                                .build()
                                )
                                .setWebpushConfig(
                                        WebpushConfig.builder()
                                                .putHeader("Urgency", "high")
                                                .putHeader("priority", "high")
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

    @Override
    public void sendNotificationToUser(ID fromUserId, String message, ID toUserId) {
        userRepository.findById(toUserId).ifPresent(user -> {
            Boolean isBenef = user.getRoles().contains(Role.COMMON_BENEFICIARY_ROLE_NAME) || user.getRoles().stream().anyMatch(role -> role.getName().contains("Bénéficiaire"));
            try {
                String username = isBenef ? "Croix-rouge" : userRepository.findById(fromUserId).get().getUsername();
                Notification notificatdion = Notification.builder()
                        .setTitle("Nouveau message de " + username)
                        .setBody(message)
                        .build();
                firebaseMessaging.send(
                        Message.builder()
                                .putData("title", "Nouveau message de " + username)
                                .putData("body", message)
                                .setAndroidConfig(
                                        AndroidConfig.builder()
                                                .setPriority(AndroidConfig.Priority.HIGH)
                                                .build()
                                )
                                .setApnsConfig(
                                        ApnsConfig.builder()
                                                .putHeader("apns-priority", "5")
                                                .setAps(Aps.builder().setContentAvailable(true).build())
                                                .build()
                                )
                                .setWebpushConfig(
                                        WebpushConfig.builder()
                                                .putHeader("priority", "high")
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
