package fr.croixrouge.exposition.controller;


import fr.croixrouge.exposition.dto.core.Donation;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.core.LoginResponse;
import fr.croixrouge.exposition.error.EmailNotConfirmError;
import fr.croixrouge.exposition.error.ErrorHandler;
import fr.croixrouge.exposition.error.UserNotValidatedByUL;
import fr.croixrouge.service.*;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController extends ErrorHandler {

    private final AuthenticationService service;
    private final MailService mailService;
    private final IFireBaseService fireBaseService;

    public LoginController(AuthenticationService service, VolunteerService volunteerService, MailService mailService, IFireBaseService fireBaseService) {
        this.service = service;
        this.mailService = mailService;
        this.fireBaseService = fireBaseService;
    }

    @PostMapping(value = "/volunteer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> volunteerLogin(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = service.authenticateVolunteer(loginRequest.getUsername(), loginRequest.getPassword());
            fireBaseService.updateFirebaseToken(loginRequest.getFirebaseToken(), loginRequest.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EmailNotConfirmError e) {
            return ResponseEntity.status(490).build();
        } catch (UserNotValidatedByUL e) {
            return ResponseEntity.status(491).build();
        }
    }

    @PostMapping(value = "/beneficiary", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> beneficiaryLogin(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = service.authenticateBeneficiary(loginRequest.getUsername(), loginRequest.getPassword());
            fireBaseService.updateFirebaseToken(loginRequest.getFirebaseToken(), loginRequest.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EmailNotConfirmError e) {
            return ResponseEntity.status(490).build();
        } catch (UserNotValidatedByUL e) {
            return ResponseEntity.status(491).build();
        }
    }

    @GetMapping(value = "/token", produces = "application/json")
    public ResponseEntity<Void> isLogin() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/sendEmailForSupport", produces = "application/json")
    public ResponseEntity<Void> logout(@RequestBody Donation donation) throws MessagingException {
        mailService.sendEmailForSupport(donation);
        return ResponseEntity.ok().build();
    }

    //cheat code to send notification to all users
    @GetMapping(value = "/sendNotification", produces = "application/json")
    public ResponseEntity<Void> sendNotification() throws MessagingException {
        fireBaseService.sendNotificationCheat();
        return ResponseEntity.ok().build();
    }

}
