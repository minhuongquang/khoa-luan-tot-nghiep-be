package com.stc.thamquan.securities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 17:39
 * Filename  : JwtUserDetails
 */
public class JwtUserDetails implements UserDetails {

    private final String hoTen;

    private final String username;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean trangThai;

    public JwtUserDetails(String hoTen, String username,
                          String password, Collection<? extends GrantedAuthority> authorities, boolean trangThai) {
        this.hoTen = hoTen;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.trangThai = trangThai;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getHoTen() {
        return hoTen;
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
        return trangThai;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return trangThai;
    }
}