package xyz.scantag.dev.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.scantag.dev.api.entity.TagProfile;
import xyz.scantag.dev.api.entity.User;

import java.util.List;

@Repository
public interface TagProfileRepository extends JpaRepository<TagProfile, String> {

    List<TagProfile> findAllByUserId(String userId);

}
