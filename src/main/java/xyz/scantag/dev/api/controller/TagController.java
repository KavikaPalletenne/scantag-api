package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.scantag.dev.api.entity.Tag;
import xyz.scantag.dev.api.model.TagModel;
import xyz.scantag.dev.api.service.TagService;
import xyz.scantag.dev.api.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/v1/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Object> createTag(@RequestParam String userId, @RequestBody TagModel tagModel) {

        return tagService.createTag(userId, tagModel);
    }

    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Object> updateTag(@RequestParam String tagId, @RequestBody TagModel tagModel, Principal principal) {

        if(!userService.getByUsername(principal.getName()).getUserId().equals(tagService.getById(tagId).getUserId())) {

            return ResponseEntity.badRequest().body("Unauthorised to update tag");
        }

        return tagService.updateTag(tagId, tagModel);

    }

    @GetMapping(value = "/getAllByUserId")
    public List<Tag> getAllByUserId(@RequestParam String userId, Principal principal) {

        if(!principal.getName().equals(userService.getById(userId).getEmail())) {

            return null;
        }

        return tagService.getAllByUserId(userId);
    }

    @GetMapping(value = "/get")
    public Tag getById(@RequestParam String tagId) {

        return tagService.getById(tagId);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<Object> deleteById(@RequestParam String tagId, Principal principal){

        Tag tag = tagService.getById(tagId);

        if(tag == null) {
            return ResponseEntity.badRequest().body("Could not find tag");
        }

        if(!principal.getName().equals(userService.getById(tag.getUserId()).getEmail())) {
            return ResponseEntity.badRequest().body("Unauthorised to delete tag");
        }

        return tagService.deleteByTagId(tagId);
    }
}
