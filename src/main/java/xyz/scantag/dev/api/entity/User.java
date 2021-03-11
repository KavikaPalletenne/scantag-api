package xyz.scantag.dev.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_user")
@Getter @Setter
public class User {

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
}
