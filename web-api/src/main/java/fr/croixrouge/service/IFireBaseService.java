package fr.croixrouge.service;

public interface IFireBaseService {
    void updateFirebaseToken(String firebaseToken, String username);
    void sendNotificationCheat();
}
