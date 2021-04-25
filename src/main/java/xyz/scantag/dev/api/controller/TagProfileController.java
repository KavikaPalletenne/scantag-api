package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.scantag.dev.api.entity.TagProfile;
import xyz.scantag.dev.api.model.TagProfileModel;
import xyz.scantag.dev.api.service.TagProfileService;
import xyz.scantag.dev.api.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/tags")
public class TagProfileController {

    @Autowired
    private TagProfileService tagProfileService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Object> createTagProfile(@RequestParam String userId, @RequestBody TagProfileModel tagProfileModel) {

        return tagProfileService.createTagProfile(userId, tagProfileModel);
    }

    @GetMapping(value = "/getAllByUserId")
    public List<TagProfile> getAllByUserId(@RequestParam String userId, Principal principal) {

        if(!principal.getName().equals(userService.getById(userId).getEmail())) {

            return null;
        }

        return tagProfileService.getAllByUserId(userId);
    }

    @GetMapping(value = "/get")
    public TagProfile getById(@RequestParam String profileId) {

        return tagProfileService.getById(profileId);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<Object> deleteById(@RequestParam String profileId, Principal principal){

        TagProfile tagProfile = tagProfileService.getById(profileId);

        if(tagProfile == null) {
            return ResponseEntity.badRequest().body("Could not find profile");
        }

        if(!principal.getName().equals(userService.getById(tagProfile.getUserId()).getEmail())) {
            return ResponseEntity.badRequest().body("Unauthorised to delete profile");
        }

        return tagProfileService.deleteByProfileId(profileId);
    }
}
