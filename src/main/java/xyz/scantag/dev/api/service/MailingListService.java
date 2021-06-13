package xyz.scantag.dev.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.scantag.dev.api.entity.MailingListEntry;
import xyz.scantag.dev.api.persistence.MailingListRepository;

@Transactional
@Service
public class MailingListService {

    @Autowired
    private MailingListRepository mailingListRepository;

    public ResponseEntity<Object> signUp(String email) {

        if(mailingListRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Already signed up to mailing list");
        }

        MailingListEntry signUp = MailingListEntry.builder()
                            .email(email)
                            .build();

        mailingListRepository.save(signUp);

        return ResponseEntity.ok().body("Successfully added to mailing list.");
    }

    public MailingListEntry get(String email) {

        if(mailingListRepository.findByEmail(email).isEmpty()) {
            return MailingListEntry.builder()
                    .email("empty")
                    .build();
        }

        return mailingListRepository.findByEmail(email).get();
    }

    public ResponseEntity<Object> unsubscribe(String email) {

        if(mailingListRepository.findByEmail(email).isPresent()) {
            mailingListRepository.deleteByEmail(email);
            if(mailingListRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.unprocessableEntity().body("Unable to delete mailing list entry");
            }
            return ResponseEntity.ok().body("Successfully deleted entry from mailing list");
        }

        return ResponseEntity.badRequest().body("No such entry in mailing list");
    }
}
