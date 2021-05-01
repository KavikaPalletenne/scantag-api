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
public class Tag {

    @Id
    @Column(length = 12)
    private String tagId;

    @Column(length = 50)
    private String tagName;

    @Column(length = 320)
    private String email;

    @Column(length = 8)
    private String userId;

    @Column(length = 300)
    private String firstName;

    @Column(length = 300)
    private String lastName;

    @Column(length = 50)
    private String contactNumber;

    @Column(length = 200)
    private String info;

    @Column(length = 250)
    private String address;

    private Boolean enableNotifications;

}
