package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_user")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
public class User {

    @Id
    @Column(length = 8)
    private String userId;

    @Column(length = 150)
    private String username;

    private String password;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    private Integer contactNumber;

    @Column(length = 200)
    private String info;

    // Address Variables

    @Column(length = 250)
    private String address;

    // Admin Variables
    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @Column(length = 7)
    private String role;

    private Boolean enableNotifications;
}
