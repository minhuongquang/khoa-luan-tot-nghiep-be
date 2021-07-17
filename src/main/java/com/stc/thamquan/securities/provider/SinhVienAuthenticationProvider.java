package com.stc.thamquan.securities.provider;

import com.stc.thamquan.exceptions.UserNotFoundAuthenticationException;
import com.stc.thamquan.securities.SinhVienDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 17:56
 * Filename  : SinhVienAuthenticationProvider
 */
@Slf4j
@Service
public class SinhVienAuthenticationProvider implements AuthenticationProvider {

    private final SinhVienDetailsService sinhVienDetailsService;

    public SinhVienAuthenticationProvider(SinhVienDetailsService sinhVienDetailsService) {
        this.sinhVienDetailsService = sinhVienDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SinhVienAuthenticationToken token = (SinhVienAuthenticationToken) authentication;
        String email = token.getName();
        boolean verifyCredentials = Boolean.parseBoolean(token.isVerifyCredentials().toString());
        UserDetails userDetails;
        try {
            userDetails = sinhVienDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException ex) {
            throw new UserNotFoundAuthenticationException(ex.getMessage());
        }
        if (!userDetails.isEnabled())
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        return new TaiKhoanAuthenticationToken(email, "N/A", verifyCredentials, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(SinhVienAuthenticationToken.class);
    }
}
