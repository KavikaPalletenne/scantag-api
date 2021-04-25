package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "t_tag_profile")
@SuperBuilder
@NoArgsConstructor
public class TagProfile {

    @Id
    @Column(length = 12)
    private String profileId;

    private String userId;

    private Boolean showEmail;

    private Boolean showName;

    private Boolean showContactNumber;

    private Boolean showInfo;

    private Boolean showAddress;

}
