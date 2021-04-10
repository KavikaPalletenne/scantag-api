package xyz.scantag.dev.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserModel {

    private String userId;

    private String username;
    private String password;
    private String email;

    private Integer contactNumber;

    private String info;

    // Address Variables
    private String address;

    // Admin Variables
    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    private String role;
}
