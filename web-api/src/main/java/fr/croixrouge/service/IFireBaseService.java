package fr.croixrouge.service;

import fr.croixrouge.domain.model.ID;

public interface IFireBaseService {
    void updateFirebaseToken(String firebaseToken, String username);
    void sendNotificationCheat();

    void sendNotificationToUser(ID fromUser, String message, ID toUser);
}
