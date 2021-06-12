package xyz.scantag.dev.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.UserModel;
import xyz.scantag.dev.api.persistence.UserRepository;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptEncoder;

    @Autowired
    private  EmailService emailService;


    public User getById(String userId) {

        if(userRepository.findById(userId).isEmpty()) {
            return User.builder()
                    .email("empty")
                    .build();
        }

        return userRepository.findById(userId).get();
    }

    public User getByUsername(String username) {

        if(userRepository.findByUsername(username).isEmpty()) {
            return User.builder()
                    .email("empty")
                    .build();
        }

        if(!isUserEmailVerifiedByUsername(username)) {
            return User.builder()
                    .email("emailnotverified")
                    .build();
        }

        return userRepository.findByUsername(username).get();
    }

    public ResponseEntity<Object> createUser(UserModel userModel) {

        if(userRepository.findByUsername(userModel.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is taken");
        }
        else if(userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("An account with this email already exists");
        }

        String userId = RandomStringUtils.randomAlphanumeric(8);

        while(userRepository.findById(userId).isPresent()) {
            userId = RandomStringUtils.randomAlphanumeric(8);
        }

        Integer maxTags = 1;

        if(userModel.getRole() != null) {
            if (userModel.getRole().equals("premium")) {
                maxTags = 30;
            }
        }

        String emailVerificationToken = RandomStringUtils.randomAlphanumeric(30);

        while(userRepository.findByEmailVerificationToken(emailVerificationToken).isPresent()) {
            emailVerificationToken = RandomStringUtils.randomAlphanumeric(30);
        }

        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .maxTags(maxTags)
                .usedTags(0)
                .accountActive(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .role(userModel.getRole())
                .enableNotifications(userModel.getEnableNotifications())
                .emailVerificationToken(emailVerificationToken)
                .emailVerified(false)
                .accountCreationDate(LocalDateTime.now(Clock.systemUTC()))
                .build();

        try {
            String emailVerificationLink = "https://scantag.co/auth/verify-email?token=" + emailVerificationToken;
            emailService.sendEmailVerificationEmail(user.getEmail(), emailVerificationLink);
        } catch (UnsupportedEncodingException | MessagingException e) {

            return ResponseEntity.ok().body("Error while sending email verification email");
        }

        userRepository.save(user);
        return ResponseEntity.ok().body("User successfully created");
    }

    public ResponseEntity<Object> updateUser(String userId, UserModel userModel) {

        User oldUser = userRepository.findByUsername(userModel.getEmail()).get();
        String oldPassword = oldUser.getPassword();

        Integer maxTags = 1;

        if(userModel.getRole() != null) {
            if (userModel.getRole().equals("premium")) {
                maxTags = 30;
            }
        }

        if(userModel.getRole() == null) {
            userModel.setRole(oldUser.getRole());

            if(oldUser.getRole().equals("premium")) {
                maxTags = 30;
            }
        }

        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(oldPassword)
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .maxTags(maxTags)
                .usedTags(oldUser.getUsedTags())
                .accountActive(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .role(userModel.getRole())
                .enableNotifications(oldUser.getEnableNotifications())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok().body("User successfully updated");
    }

    public ResponseEntity<Object> updatePassword(String username, String oldPassword, String newPassword) {

        if(userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        User user = userRepository.findByUsername(username).get();

        if(!bCryptEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        user.setPassword(bCryptEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully updated password");
    }

    public ResponseEntity<Object> enableNotifications(String username, Boolean enableNotifications) {

        if(userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("User not found");
        }

        User user = userRepository.findByUsername(username).get();

        user.setEnableNotifications(enableNotifications);
        userRepository.save(user);

        return ResponseEntity.ok().body("Enabled notifications");
    }

    public void updateResetPasswordToken(String token, String email) {

        if(userRepository.findByEmail(email).isEmpty()) {
            return;
        }

        User user = userRepository.findByEmail(email).get();
        user.setResetPasswordToken(token);
        userRepository.save(user);
    }

    public User getByResetPasswordToken(String token) {

        if(userRepository.findByResetPasswordToken(token).isEmpty())
        {
            User user = User.builder()
                    .email("empty")
                    .build();

            return user;
        }

        return userRepository.findByResetPasswordToken(token).get();
    }

    public ResponseEntity<Object> updatePassword(User user, String newPassword) {

        String encodedPassword = bCryptEncoder.encode(newPassword);

        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully updated password");
    }

    public ResponseEntity<Object> verifyEmail(String token) {

        if(userRepository.findByEmailVerificationToken(token).isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("Could not find user associated with token");
        }

        User user = userRepository.findByEmailVerificationToken(token).get();

        if(isUserEmailVerifiedByUserId(user.getUserId())) {
            return ResponseEntity.badRequest().body("Email has already been verified");
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully validated email");
    }

    public Boolean isUserEmailVerifiedByUserId(String userId) {
        return userRepository.findById(userId).get().getEmailVerified();
    }
    public Boolean isUserEmailVerifiedByUsername(String username) {
        return userRepository.findByUsername(username).get().getEmailVerified();
    }

    public ResponseEntity<Object> deleteUser(String username) {

        if(userRepository.findByUsername(username).isPresent()) {

            List<User> deletedUsers = userRepository.deleteByUsername(username);

            if(userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.unprocessableEntity().body("Failed to delete user");
            }
            return ResponseEntity.ok().body("Successfully deleted user");
        }

        return ResponseEntity.unprocessableEntity().body("User not found");
    }
}
