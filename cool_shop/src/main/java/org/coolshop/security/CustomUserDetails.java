package org.coolshop.security;


import org.coolshop.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// Use a separate adapter to avoid dependency of User on Spring Security.
public class CustomUserDetails implements UserDetails {

    private User user;
    private Collection<SimpleGrantedAuthority> authorities;


    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getModelUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList();
            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
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
        return true;
    }
}
