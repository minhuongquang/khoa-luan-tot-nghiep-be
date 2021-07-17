package com.stc.thamquan.securities;

import com.stc.thamquan.entities.TaiKhoan;
import com.stc.thamquan.repositories.TaiKhoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 17:49
 * Filename  : TaiKhoanDetailsService
 */
@Slf4j
@Service
public class TaiKhoanDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanDetailsService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Tài khoản có email %s không tồn tại", email)));
        return getUserDetails(taiKhoan);
    }

    private JwtUserDetails getUserDetails(TaiKhoan taiKhoan) {
        return new JwtUserDetails(
                taiKhoan.getHoTen(),
                taiKhoan.getEmail(),
                taiKhoan.getPassword(),
                taiKhoan.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                taiKhoan.isTrangThai()
        );
    }
}
