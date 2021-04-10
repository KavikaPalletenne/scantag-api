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

    @GetMapping(value = "/get/current")
    public User getCurrentUser(Principal principal) {

        return userService.getByUsername(principal.getName());
    }

    //TODO: Endpoint for "forgot password"

    @PostMapping(value = "/update/enableNotifications")
    public ResponseEntity<Object> enableNotifications(Principal principal, @RequestParam Boolean enableNotifications) {

        return userService.enableNotifications(principal.getName(), enableNotifications);
    }

    @PostMapping(value = "/delete")
    public  ResponseEntity<Object> deleteUser(Principal principal) {

        return userService.deleteUser(userService.getByUsername(principal.getName()).getUserId());
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> updateUser(@RequestParam String id, @RequestBody UserModel userModel, Principal principal) {
//        TODO: Add principal authentication before updating user after implementing
//        Spring Security
        if(principal.getName().equals(userService.getById(id).getUsername())) {
            return userService.updateUser(id, userModel);
        }

        return ResponseEntity.badRequest().body("Unauthorised to update user");
    }

}
