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

import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptEncoder;

    public User getById(String userId) {

        User user = userRepository.findById(userId).get();

        if(user.getUserId() == null) {
            User emptyUser = User.builder().email("empty")
                    .build();

            return emptyUser;
        }

        return user;
    }

    public User getByUsername(String username) {

        User user = userRepository.findByUsername(username).get();

        if(user.getUserId() == null) {

            User emptyUser = User.builder()
                    .email("empty")
                    .build();

            return emptyUser;
        }

        return user;
    }

    public ResponseEntity<Object> createUser(UserModel userModel) {

        if(userRepository.findByUsername(userModel.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is taken");
        }
        else if(userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("An account with this email already exists");
        }

        String userId = RandomStringUtils.randomAlphanumeric(8);

        while (userRepository.findById(userId).isPresent()) {
            userId = RandomStringUtils.randomAlphanumeric(8);
        }

        Integer maxTags = 1;

        if(userModel.getRole() != null) {
            if (userModel.getRole().equals("premium")) {
                maxTags = 30;
            }
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
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().body("User successfully created");
    }

    public ResponseEntity<Object> updateUser(String userId, UserModel userModel) {

        User oldUser = userRepository.findByUsername(userModel.getEmail()).get();

        if(oldUser.getUserId() == null) {

            return ResponseEntity.unprocessableEntity().body("User not found");
        }

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

        User user = userRepository.findByUsername(username).get();

        if(user.getUserId() == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        if(!bCryptEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        user.setPassword(bCryptEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully updated password");
    }

    public ResponseEntity<Object> enableNotifications(String username, Boolean enableNotifications) {

        User user = userRepository.findByUsername(username).get();

        if(user.getUserId() == null) {
            return ResponseEntity.unprocessableEntity().body("User not found");
        }

        user.setEnableNotifications(enableNotifications);
        userRepository.save(user);

        return ResponseEntity.ok().body("Enabled notifications");
    }

    public void updateResetPasswordToken(String token, String email) {

        User user = userRepository.findByEmail(email).get();

        if(user.getUserId() == null) {
            return;
        }

        user.setResetPasswordToken(token);
        userRepository.save(user);
    }

    public User getByResetPasswordToken(String token) {

        User user = userRepository.findByResetPasswordToken(token).get();

        if(user.getUserId() == null)
        {
            User emptyUser = User.builder()
                    .email("empty")
                    .build();

            return emptyUser;
        }

        return user;
    }

    public ResponseEntity<Object> updatePassword(User user, String newPassword) {

        String encodedPassword = bCryptEncoder.encode(newPassword);

        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully updated password");
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
