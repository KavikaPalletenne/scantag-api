package xyz.scantag.dev.api.model;


import lombok.Getter;
import lombok.Setter;
import xyz.scantag.dev.api.entity.UserRole;

import java.util.List;

@Getter @Setter
public class UserModel {

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

    private List<UserRole> roles;
}
