package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.UserModel;
import xyz.scantag.dev.api.service.UserService;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping(value = "api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Object> createUser(@RequestBody UserModel userModel) {

        log.info("Creating user {}", userModel.getUsername());
        return userService.createUser(userModel);
    }

    @GetMapping(value = "/get")
    public User getUser(@RequestParam String id) {

        return userService.getById(id);
    }

    @PostMapping(value = "/delete")
    public  ResponseEntity<Object> deleteUser(@RequestParam String id) {
//        TODO: Add principal authentication before deleting user after implementing
//        Spring Security
        return userService.deleteUser(id);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> updateUser(@RequestParam String id, @RequestBody UserModel userModel) {
//        TODO: Add principal authentication before updating user after implementing
//        Spring Security
        return userService.updateUser(id, userModel);
    }

}
