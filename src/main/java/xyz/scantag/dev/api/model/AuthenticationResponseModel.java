package xyz.scantag.dev.api.model;

import lombok.Getter;

@Getter
public class AuthenticationResponseModel {

    private final String jwt;

    private final String userId;

    public AuthenticationResponseModel(String jwt, String userId) {
        this.jwt = jwt;
        this.userId = userId;
    }
}
