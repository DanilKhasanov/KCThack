package com.hackathon.KDT_HACK.Registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private String id;
    private String name;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;



    public static UserDetailsImpl build(User user){
        return new UserDetailsImpl(

                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getStatus()

        );


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

         return Collections.singletonList(
                new SimpleGrantedAuthority(role.getAuthority())
                );
    }

    @Override
    public @Nullable String getPassword() {
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

        return status != UserStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return status == UserStatus.ACTIVE;
    }
}
