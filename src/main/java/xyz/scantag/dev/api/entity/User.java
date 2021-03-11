package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
public class User {

    @Id
    String userId;

    String username;
    String password;
    String email;

    Integer contactNumber;

    // Address Variables
    String streetAddress;
    String city;
    Integer postcode;
    String state;
    String country;

    // Admin Variables
    Boolean accountActive;
    Boolean accountNonLocked;
    Boolean credentialsNonExpired;
}
