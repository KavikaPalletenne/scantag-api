package xyz.scantag.dev.api.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.scantag.dev.api.entity.TagProfile;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.model.TagProfileModel;
import xyz.scantag.dev.api.persistence.TagProfileRepository;
import xyz.scantag.dev.api.persistence.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class TagProfileService {

    @Autowired
    private TagProfileRepository tagProfileRepository;

    @Autowired
    private UserRepository userRepository;



    public ResponseEntity<Object> createTagProfile(String userId, TagProfileModel tagProfileModel) {

        if(!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        User user = userRepository.findById(userId).get();

        if(user.getUsedProfiles() >= user.getMaxProfiles()) {
            return ResponseEntity.badRequest().body("Maximum profiles exceeded");
        }

        String profileId = RandomStringUtils.randomAlphanumeric(12);

        while (tagProfileRepository.findById(profileId).isPresent()) {
            profileId = RandomStringUtils.randomAlphanumeric(12);
        }

        TagProfile tagProfile = TagProfile.builder()
                .profileId(profileId)
                .userId(userId)
                .showEmail(tagProfileModel.getShowEmail())
                .showName(tagProfileModel.getShowName())
                .showContactNumber(tagProfileModel.getShowContactNumber())
                .showInfo(tagProfileModel.getShowInfo())
                .showAddress(tagProfileModel.getShowAddress())
                .build();

        tagProfileRepository.save(tagProfile);

        return ResponseEntity.ok().body("Successfully created tag profile");
    }

    public List<TagProfile> getByUserId(String userId) {

        return tagProfileRepository.findAllByUserId(userId);
    }

    public TagProfile getById(String profileId) {

        if(tagProfileRepository.findById(profileId).isEmpty()) {
            return null;
        }

        return tagProfileRepository.findById(profileId).get();
    }

    public ResponseEntity<Object> deleteByProfileId(String profileId) {

        if (!userRepository.existsById(profileId)) {
            return ResponseEntity.unprocessableEntity().body("Profile does not exist");
        }

        TagProfile tagProfile = tagProfileRepository.findById(profileId).get();

        User user = userRepository.findById(tagProfile.getUserId()).get();

        tagProfileRepository.deleteById(profileId);

        if (userRepository.existsById(profileId)) {
            return ResponseEntity.unprocessableEntity().body("Unable to delete profile");
        }

        user.setUsedProfiles(user.getUsedProfiles() - 1);

        userRepository.save(user);

        return ResponseEntity.ok().body("Successfully delete profile");
    }
}
