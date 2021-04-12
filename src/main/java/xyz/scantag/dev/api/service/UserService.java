package xyz.scantag.dev.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.UserModel;
import xyz.scantag.dev.api.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptEncoder;

    public User getById(String userId) {

        if(userRepository.findById(userId).isEmpty()) {
            return null;
        }

        return userRepository.findById(userId).get();
    }

    public User getByUsername(String username) {

        if(userRepository.findByUsername(username).isEmpty()) {
            return null;
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

        String userId = RandomStringUtils.randomAlphanumeric(8);;

        while (true) {
            if(userRepository.findById(userId).isEmpty()) {
                break;
            }
            userId = RandomStringUtils.randomAlphanumeric(8);
        }


        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .contactNumber(userModel.getContactNumber())
                .info(userModel.getInfo())
                .address(userModel.getAddress())
                .accountActive(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .role(userModel.getRole())
                .enableNotifications(true)
                .build();

        userRepository.save(user);

        log.info("User {} successfully created", user.getUsername());
        return ResponseEntity.ok().body("User successfully created");
    }

    public ResponseEntity<Object> updateUser(String userId, UserModel userModel) {

        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .contactNumber(userModel.getContactNumber())
                .info(userModel.getInfo())
                .address(userModel.getAddress())
                .accountActive(userModel.getAccountActive())
                .accountNonLocked(userModel.getAccountNonLocked())
                .credentialsNonExpired(userModel.getCredentialsNonExpired())
                .role(userModel.getRole())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok().body("User successfully updated");
    }

    public ResponseEntity<Object> enableNotifications(String username, Boolean enableNotifications) {

        if(userRepository.findByUsername(username).isEmpty()) {
            return ResponseEntity.unprocessableEntity().body("User not found");
        }

        User user = userRepository.findByUsername(username).get();

        user.setEnableNotifications(enableNotifications);
        userRepository.save(user);

        return ResponseEntity.ok().body("Enabled notifcations");
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

        return userRepository.findByResetPasswordToken(token).get();
    }

    public ResponseEntity<Object> updatePassword(User user, String newPassword) {

        String encodedPassword = bCryptEncoder.encode(newPassword);

        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);

        return ResponseEntity.ok().body("Password updated");
    }

    public ResponseEntity<Object> deleteUser(String userId) {

        if(userRepository.findById(userId).isPresent()) {

            userRepository.deleteById(userId);

            if(userRepository.findById(userId).isPresent()) {
                log.warn("Failed to delete user {}", userId);
                return ResponseEntity.unprocessableEntity().body("Failed to delete user");
            }
            log.info("Deleted user {}", userId);
            return ResponseEntity.ok().body("Deleted user successfully");
        }

        log.warn("User not found");
        return ResponseEntity.unprocessableEntity().body("User not found");
    }
}
