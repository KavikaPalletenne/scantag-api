package xyz.scantag.dev.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.scantag.dev.api.entity.MailingListEntry;
import xyz.scantag.dev.api.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailingListRepository extends JpaRepository<MailingListEntry, String> {

    Optional<MailingListEntry> findByEmail(String email);

    List<User> deleteByEmail(String username);

}