package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.UserModel;
import xyz.scantag.dev.api.persistence.UserRepository;
import xyz.scantag.dev.api.service.EmailService;
import xyz.scantag.dev.api.service.UserService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;


    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Object> createUser(@RequestBody UserModel userModel) {

        log.info("Creating user {}", userModel.getUsername());
        return userService.createUser(userModel);
    }

    @GetMapping(value = "/get")
    public User getUser(@RequestParam String id) {

        return userService.getById(id);
    }

    @GetMapping(value="/getByEmail")
    public User getUserByEmail(@RequestParam String email) {

        return userService.getByUsername(email);
    }

    @GetMapping(value = "/get/current")
    public User getCurrentUser(Principal principal) {

        return userService.getByUsername(principal.getName());
    }

    @PostMapping(value = "/update/enableNotifications")
    public ResponseEntity<Object> enableNotifications(Principal principal, @RequestParam Boolean enableNotifications) {

        return userService.enableNotifications(principal.getName(), enableNotifications);
    }

    @PostMapping(value = "/delete")
    public  ResponseEntity<Object> deleteUser(Principal principal) {

        return userService.deleteUser(principal.getName());
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> updateUser(@RequestParam String id, @RequestBody UserModel userModel, Principal principal) {

        if(principal.getName().equals(userService.getById(id).getUsername())) {

            return userService.updateUser(id, userModel);
        }

        return ResponseEntity.badRequest().body("Unauthorised to update user");
    }

    @PostMapping(value = "/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, Principal principal) {

        return userService.updatePassword(principal.getName(), oldPassword, newPassword);
    }


    // Forgot Password Functionality

    @PostMapping("/auth/forgotPassword")
    public ResponseEntity<Object> processForgotPassword(@RequestParam String email) {

        if(userService.getByUsername(email) == null) {
            return ResponseEntity.ok().body("If your email has been used to register an account, a reset password link has been sent to your email.");
        }

        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = System.getenv("FRONTEND_URL") + "/auth/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetPasswordLink);
        } catch (UnsupportedEncodingException | MessagingException e) {

            return ResponseEntity.ok().body("Error while sending email");
        }

        return ResponseEntity.ok().body("If your email has been used to register an account, a reset password link has been sent to your email.");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> processResetPassword(@RequestParam String token, @RequestParam String password) {

        if(userService.getByResetPasswordToken(token) == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = userService.getByResetPasswordToken(token);
        return userService.updatePassword(user, password);
    }

}
