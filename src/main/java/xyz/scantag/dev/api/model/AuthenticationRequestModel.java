package xyz.scantag.dev.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class AuthenticationRequestModel {

    private String username;
    private String password;

    public AuthenticationRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
