package xyz.scantag.dev.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.scantag.dev.api.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    List<Tag> findAllByUserId(String userId);

}
