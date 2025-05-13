package com.hcmute.myanime.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class AuthenticationResponseDTO {

    private String jwt;
    private String username;
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public AuthenticationResponseDTO(String jwt, String username, String authority) {
        this.jwt = jwt;
        this.username = username;
        this.authority = authority;
    }
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getJwt() {
        return jwt;
    }
}
