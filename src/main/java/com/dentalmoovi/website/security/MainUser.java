package com.dentalmoovi.website.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.repositories.RolesRep;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MainUser implements UserDetails{

    private String cacheRef;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static MainUser build(Users user, RolesRep rolesRep, String ref){
        Set<Long> rolesIds = user.getRolesIds();

        @SuppressWarnings("null")
        List<Roles> roles = rolesIds.stream()
            .map(idRole -> rolesRep.findById(idRole)
                .orElseThrow(() -> new RuntimeException("Role not found")))
            .collect(Collectors.toList());

        List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
            .collect(Collectors.toList());

        String cacheRef = String.valueOf(user.getId() + ref.length());

        return new MainUser(cacheRef, user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public String getCacheRef(){ return cacheRef; }

}
