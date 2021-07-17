package com.stc.thamquan.securities;

import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.entities.TaiKhoan;
import com.stc.thamquan.repositories.CongTacVienRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 17:49
 * Filename  : CongTacVienDetailsService
 */
@Slf4j
@Service
public class CongTacVienDetailsService implements UserDetailsService {

    private final CongTacVienRepository congTacVienRepository;

    public CongTacVienDetailsService(CongTacVienRepository congTacVienRepository) {
        this.congTacVienRepository = congTacVienRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CongTacVien congTacVien = congTacVienRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Tài khoản có email %s không tồn tại", email)));
        return getUserDetails(congTacVien);
    }

    private JwtUserDetails getUserDetails(CongTacVien congTacVien) {
        return new JwtUserDetails(
                congTacVien.getHoTen(),
                congTacVien.getEmail(),
                congTacVien.getPassword(),
                congTacVien.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                congTacVien.isTrangThai()
        );
    }
}
