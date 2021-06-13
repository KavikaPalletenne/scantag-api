package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_mailingList")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MailingListEntry {

    @Id
    @Column(length = 320)
    private String email;
}
