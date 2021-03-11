package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.scantag.dev.api.model.UserModel;
import xyz.scantag.dev.api.service.UserService;

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

}
