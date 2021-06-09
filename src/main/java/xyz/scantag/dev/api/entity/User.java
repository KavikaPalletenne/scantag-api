package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
public class User {

    @Id
    @Column(length = 8)
    private String userId;

    @Column(length = 320)
    private String username;

    @Column(length = 60)
    private String password;

    @Column(length = 320)
    private String email;

    @Column(length = 300)
    private String firstName;

    @Column(length = 300)
    private String lastName;

    // Profiles feature
    private Integer maxTags;
    private Integer usedTags;

    // Admin Variables
    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @Column(length = 7)
    private String role;

    private Boolean enableNotifications;

    @Column(length = 30)
    private String resetPasswordToken;

    // Web3 SSO Support
    @Column(length = 42) // ETH Address max 42 characters. Change max value to support other blockchains.
    private String publicAddress;

    private long nonce;
}
