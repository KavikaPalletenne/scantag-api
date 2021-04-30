package xyz.scantag.dev.api.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.scantag.dev.api.entity.Tag;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.TagModel;
import xyz.scantag.dev.api.persistence.TagRepository;
import xyz.scantag.dev.api.persistence.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;



    public ResponseEntity<Object> createTag(String userId, TagModel tagModel) {

        if(!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        User user = userRepository.findById(userId).get();

        if(user.getUsedTags() >= user.getMaxTags()) {
            return ResponseEntity.badRequest().body("Maximum tag limit exceeded");
        }

        String tagId = RandomStringUtils.randomAlphanumeric(12);

        while (tagRepository.findById(tagId).isPresent()) {
            tagId = RandomStringUtils.randomAlphanumeric(12);
        }

        Tag tag = Tag.builder()
                .tagId(tagId)
                .profileName(tagModel.getProfileName())
                .userId(userId)
                .email(tagModel.getEmail())
                .profileName(tagModel.getProfileName())
                .firstName(tagModel.getFirstName())
                .lastName(tagModel.getLastName())
                .contactNumber(tagModel.getContactNumber())
                .info(tagModel.getInfo())
                .address(tagModel.getAddress())
                .build();

        tagRepository.save(tag);

        user.setUsedTags(user.getUsedTags() + 1);

        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully created tag");
    }

    public ResponseEntity<Object> updateTag(String tagId, TagModel tagModel) {

        if(!tagRepository.existsById(tagId)) {
            return ResponseEntity.badRequest().body("Tag does not exist");
        }

        Tag tag = tagRepository.findById(tagId).get();

        Tag newTag = Tag.builder()
                .tagId(tagId)
                .profileName(tagModel.getProfileName())
                .userId(tag.getUserId())
                .email(tagModel.getEmail())
                .profileName(tagModel.getProfileName())
                .firstName(tagModel.getFirstName())
                .lastName(tagModel.getLastName())
                .contactNumber(tagModel.getContactNumber())
                .info(tagModel.getInfo())
                .address(tagModel.getAddress())
                .build();

        tagRepository.save(newTag);

        return ResponseEntity.ok().body("Successfully updated tag");
    }

    public List<Tag> getAllByUserId(String userId) {

        return tagRepository.findAllByUserId(userId);
    }

    public Tag getById(String profileId) {

        if(tagRepository.findById(profileId).isEmpty()) {
            return null;
        }

        return tagRepository.findById(profileId).get();
    }

    public ResponseEntity<Object> deleteByTagId(String tagId) {

        if (!userRepository.existsById(tagId)) {
            return ResponseEntity.unprocessableEntity().body("Tag does not exist");
        }

        Tag tag = tagRepository.findById(tagId).get();

        User user = userRepository.findById(tag.getUserId()).get();

        tagRepository.deleteById(tagId);

        if (userRepository.existsById(tagId)) {
            return ResponseEntity.unprocessableEntity().body("Unable to delete tag");
        }

        user.setUsedTags(user.getUsedTags() - 1);

        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully delete tag");
    }
}
