package xyz.scantag.dev.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter @Setter
public class TagModel {

    private String tagId;

    private String profileName;

    private String email;

    private String userId;

    private String firstName;

    private String lastName;

    private String contactNumber;

    private String info;

    private String address;

    private Boolean enableNotifications;
}
