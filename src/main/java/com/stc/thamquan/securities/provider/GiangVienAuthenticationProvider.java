package com.stc.thamquan.securities.provider;

import com.stc.thamquan.securities.GiangVienDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 17:56
 * Filename  : GiangVienAuthenticationProvider
 */
@Slf4j
@Service
public class GiangVienAuthenticationProvider implements AuthenticationProvider {

    private final GiangVienDetailsService giangVienDetailsService;

    public GiangVienAuthenticationProvider(GiangVienDetailsService giangVienDetailsService) {
        this.giangVienDetailsService = giangVienDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GiangVienAuthenticationToken token = (GiangVienAuthenticationToken) authentication;
        String email = token.getName();
        boolean verifyCredentials = Boolean.parseBoolean(token.isVerifyCredentials().toString());
        UserDetails userDetails = giangVienDetailsService.loadUserByUsername(email);
        if (!userDetails.isEnabled())
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        return new TaiKhoanAuthenticationToken(email, "N/A", verifyCredentials, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(GiangVienAuthenticationToken.class);
    }
}
