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

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptEncoder;

    public User getById(String userId) {

        if(userRepository.findById(userId).isEmpty())
        {
            log.warn("Could not find user with username - {}", userId);
            return null;
        }

        return userRepository.findById(userId).get();
    }

    public ResponseEntity<Object> createUser(UserModel userModel) {

        if(userRepository.findById(userModel.getUsername()).isPresent()) {
            log.warn("username - {} is taken", userModel.getUsername());
            return ResponseEntity.badRequest().body("Username is taken");
        }
        else if(userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            log.warn("An account with this email - {} already exists", userModel.getEmail());
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
                .username(userModel.getUsername())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .contactNumber(userModel.getContactNumber())
                .streetAddress(userModel.getStreetAddress())
                .city(userModel.getCity())
                .postcode(userModel.getPostcode())
                .state(userModel.getState())
                .country(userModel.getCountry())
                .accountActive(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        userRepository.save(user);

        log.info("User {} successfully created", user.getUsername());
        return ResponseEntity.ok().body("User successfully created");
    }

    public ResponseEntity<Object> updateUser(String userId, UserModel userModel) {

        User user = User.builder()
                .userId(userId)
                .username(userModel.getUsername())
                .password(bCryptEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .contactNumber(userModel.getContactNumber())
                .streetAddress(userModel.getStreetAddress())
                .city(userModel.getCity())
                .postcode(userModel.getPostcode())
                .state(userModel.getState())
                .country(userModel.getCountry())
                .accountActive(userModel.getAccountActive())
                .accountNonLocked(userModel.getAccountNonLocked())
                .credentialsNonExpired(userModel.getCredentialsNonExpired())
                .build();

        userRepository.save(user);
        log.info("User {} successfully updated", userId);
        return ResponseEntity.ok().body("User successfully updated");
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
