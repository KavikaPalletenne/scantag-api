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

        if(userRepository.findById(userId).isEmpty()) {
            User user = User.builder()
                    .email("empty")
                    .build();

            return user;
        }

        return userRepository.findById(userId).get();
    }

    public User getByUsername(String username) {

        if(userRepository.findByUsername(username).isEmpty()) {

            User user = User.builder()
                    .email("empty")
                    .build();

            return user;
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

        while (userRepository.findById(userId).isPresent()) {
            userId = RandomStringUtils.randomAlphanumeric(8);
        }


        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .contactNumber(userModel.getContactNumber())
                .info(userModel.getInfo())
                .address(userModel.getAddress())
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

        String oldPassword = userRepository.findByUsername(userModel.getEmail()).get().getPassword();

        User user = User.builder()
                .userId(userId)
                .username(userModel.getEmail())
                .password(oldPassword)
                .email(userModel.getEmail())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .contactNumber(userModel.getContactNumber())
                .info(userModel.getInfo())
                .address(userModel.getAddress())
                .accountActive(userModel.getAccountActive())
                .accountNonLocked(userModel.getAccountNonLocked())
                .credentialsNonExpired(userModel.getCredentialsNonExpired())
                .role(userModel.getRole())
                .enableNotifications(userModel.getEnableNotifications())
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
