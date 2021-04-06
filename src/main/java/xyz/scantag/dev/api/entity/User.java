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

    @Column(length = 50)
    private String username;

    private String password;

    @Column(length = 150)
    private String email;

    private Integer contactNumber;

    @Column(length = 200)
    private String info;

    // Address Variables

    @Column(length = 100)
    private String streetAddress;

    @Column(length = 50)
    private String city;

    private Integer postcode;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    // Admin Variables
    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @Column(length = 7)
    private String role;
}
