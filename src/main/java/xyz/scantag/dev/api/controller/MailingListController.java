package xyz.scantag.dev.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.scantag.dev.api.entity.MailingListEntry;
import xyz.scantag.dev.api.model.MailingListEntryModel;
import xyz.scantag.dev.api.service.MailingListService;

@RestController
@Slf4j
@RequestMapping(value = "/v1/mailingList")
public class MailingListController {

    @Autowired
    private MailingListService mailingListService;

    @PostMapping(value = "/signUp", consumes = "application/json")
    public ResponseEntity<Object> signUp(@RequestBody MailingListEntryModel model) {

        return mailingListService.signUp(model.getEmail());
    }

    @GetMapping(value = "/get")
    public MailingListEntry get(@RequestParam String email) {

        return mailingListService.get(email);
    }

    @PostMapping(value = "/unsubscribe", consumes = "application/json")
    public ResponseEntity<Object> unsubscribe(@RequestBody MailingListEntryModel model) {

        return mailingListService.unsubscribe(model.getEmail());
    }
}
