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
    private String userId;

    private String username;
    private String password;
    private String email;

    private Integer contactNumber;

    // Address Variables
    private String streetAddress;
    private String city;
    private Integer postcode;
    private String state;
    private String country;

    // Admin Variables
    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @ManyToOne(fetch = FetchType.EAGER)
    private List<UserRole> roles;
}
