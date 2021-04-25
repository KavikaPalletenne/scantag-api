package xyz.scantag.dev.api.model;

import lombok.Getter;
import lombok.Setter;
import xyz.scantag.dev.api.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

@Getter @Setter
public class TagProfileModel {

    private String profileId;

    private String userId;

    private Boolean showEmail;

    private Boolean showName;

    private Boolean showContactNumber;

    private Boolean showInfo;

    private Boolean showAddress;
}
