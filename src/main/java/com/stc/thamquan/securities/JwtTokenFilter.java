package com.stc.thamquan.securities;

import com.stc.thamquan.exceptions.UserNotFoundAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 18:33
 * Filename  : JwtTokenFilter
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TaiKhoanDetailsService taiKhoanDetailsService;

    @Autowired
    private GiangVienDetailsService giangVienDetailsService;

    @Autowired
    private SinhVienDetailsService sinhVienDetailsService;

    @Autowired
    private DoanhNghiepDetailsService doanhNghiepDetailsService;

    @Autowired
    private CongTacVienDetailsService congTacVienDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException e) {
                throw new UserNotFoundAuthenticationException(e.getMessage());
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.taiKhoanDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e1) {
                try {
                    userDetails = this.giangVienDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e2) {
                    try {
                        userDetails = this.congTacVienDetailsService.loadUserByUsername(username);
                    } catch (UsernameNotFoundException e3) {
                        try {
                            userDetails = this.doanhNghiepDetailsService.loadUserByUsername(username);
                        } catch (UsernameNotFoundException e4) {
                            userDetails = this.sinhVienDetailsService.loadUserByUsername(username);
                        }
                    }
                }
            }
            if (userDetails != null && jwtTokenUtils.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
