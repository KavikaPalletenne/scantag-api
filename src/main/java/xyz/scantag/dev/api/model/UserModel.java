package xyz.scantag.dev.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserModel {

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
