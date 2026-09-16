package com.backend.SyncBoard.Model;

import com.backend.SyncBoard.Enum.Roles;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetail implements UserDetails {
    
    private final String userName;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetail(User user){
        this.userName = user.getId().toString();
        this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_"+user.getRole())
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
