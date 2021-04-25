package xyz.scantag.dev.api.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserModel {

    private String userId;
    private String username;

    private String password;
    private String email;

    private String firstName;
    private String lastName;

    private String contactNumber;

    private String info;

    private String address;

    // Profiles feature
    private Integer maxProfiles;
    private Integer usedProfiles;

    private Boolean accountActive;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    private String role;

    private Boolean enableNotifications;
}
