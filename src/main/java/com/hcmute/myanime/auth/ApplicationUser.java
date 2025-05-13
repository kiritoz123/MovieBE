package com.hcmute.myanime.auth;

import com.hcmute.myanime.model.UsersEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class ApplicationUser  implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;
    private Boolean enable;

    public ApplicationUser() {
    }

    public ApplicationUser(UsersEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        Set<GrantedAuthority> role = new HashSet<>();
        role.add(new SimpleGrantedAuthority(user.getUserRoleByUserRoleId().getName()));
        this.authorities = role;
        this.enable = user.getEnable();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("SET author: " + authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
