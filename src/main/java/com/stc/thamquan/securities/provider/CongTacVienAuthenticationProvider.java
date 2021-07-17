package com.stc.thamquan.securities.provider;

import com.stc.thamquan.securities.CongTacVienDetailsService;
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
 * Filename  : TaiKhoanAuthenticationProvider
 */
@Slf4j
@Service
public class CongTacVienAuthenticationProvider implements AuthenticationProvider {

    private final CongTacVienDetailsService congTacVienDetailsService;

    public CongTacVienAuthenticationProvider(CongTacVienDetailsService congTacVienDetailsService) {
        this.congTacVienDetailsService = congTacVienDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CongTacVienAuthenticationToken token = (CongTacVienAuthenticationToken) authentication;
        String email = token.getName();
        String password = token.getCredentials() == null ? null : token.getCredentials().toString();
        boolean verifyCredentials = Boolean.parseBoolean(token.isVerifyCredentials().toString());
        UserDetails userDetails = congTacVienDetailsService.loadUserByUsername(email);
        if (!userDetails.isEnabled())
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        if (verifyCredentials) {
            assert password != null;
            if (password.equals(userDetails.getPassword())) {
                return new CongTacVienAuthenticationToken(email, password, verifyCredentials, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Sai mật khẩu");
            }
        } else {
            return new CongTacVienAuthenticationToken(email, "N/A", verifyCredentials, userDetails.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(CongTacVienAuthenticationToken.class);
    }
}
