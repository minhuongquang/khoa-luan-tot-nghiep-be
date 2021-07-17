package com.stc.thamquan.securities.provider;

import com.stc.thamquan.securities.DoanhNghiepDetailsService;
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
 * Filename  : DoanhNghiepAuthenticationProvider
 */
@Slf4j
@Service
public class DoanhNghiepAuthenticationProvider implements AuthenticationProvider {

    private final DoanhNghiepDetailsService doanhNghiepDetailsService;

    public DoanhNghiepAuthenticationProvider(DoanhNghiepDetailsService doanhNghiepDetailsService) {
        this.doanhNghiepDetailsService = doanhNghiepDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DoanhNghiepAuthenticationToken token = (DoanhNghiepAuthenticationToken) authentication;
        String email = token.getName();
        String password = token.getCredentials() == null ? null : token.getCredentials().toString();
        boolean verifyCredentials = Boolean.parseBoolean(token.isVerifyCredentials().toString());
        UserDetails userDetails = doanhNghiepDetailsService.loadUserByUsername(email);
        if (!userDetails.isEnabled())
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        if (verifyCredentials) {
            assert password != null;
            if (password.equals(userDetails.getPassword())) {
                return new TaiKhoanAuthenticationToken(email, password, verifyCredentials, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Sai mật khẩu");
            }
        } else {
            return new TaiKhoanAuthenticationToken(email, "N/A", verifyCredentials, userDetails.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DoanhNghiepAuthenticationToken.class);
    }
}
