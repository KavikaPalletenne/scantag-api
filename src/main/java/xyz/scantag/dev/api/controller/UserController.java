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
    public User getUser(@RequestParam String id, Principal principal) {

        if(principal.getName().equals(userService.getById(id).getUsername())) {
            return userService.getById(id);
        }

        // Return user with password as null to users other than user themself.
        return userService.getByIdLite(id);
    }

    // Unauthenticated Request
    @GetMapping(value = "/get")
    public User getUser(@RequestParam String id) {

        // Return user with password as null to users other than user themself.
        return userService.getByIdLite(id);
    }

    @GetMapping(value = "/get-current")
    public User get(Principal principal) {

        return userService.getByUsername(principal.getName());
    }

    @PostMapping(value = "/delete")
    public  ResponseEntity<Object> deleteUser(@RequestParam String id, Principal principal) {

        if(principal.getName().equals(userService.getById(id).getUsername())) {
            return userService.deleteUser(id);
        }

        return ResponseEntity.badRequest().body("Unauthorised to delete user");
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
