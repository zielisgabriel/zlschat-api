package br.com.gabriel.zlschat.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.gabriel.zlschat.enums.UserStatus;
import lombok.Data;

@Data
public class UserEntity implements UserDetails {
    @JsonIgnore
    private UUID id;

    private String username;

    @JsonIgnore
    private String email;
    private String password;
    private List<String> friends = new ArrayList<String>();
    private List<String> friendRequests = new ArrayList<String>();
    private UserStatus status = UserStatus.OFFLINE;

    public UserEntity() {
        this.id = UUID.randomUUID();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
