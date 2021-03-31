package xyz.scantag.dev.api.model;

import lombok.Getter;

@Getter
public class AuthenticationResponseModel {

    private final String jwt;

    public AuthenticationResponseModel(String jwt) {
        this.jwt = jwt;
    }
}
