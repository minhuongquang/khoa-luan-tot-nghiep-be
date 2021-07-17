package com.stc.thamquan.securities;

import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.repositories.GiangVienRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
 * Filename  : GiangVienDetailsService
 */
@Slf4j
@Service
public class GiangVienDetailsService implements UserDetailsService {

    private final GiangVienRepository giangVienRepository;

    public GiangVienDetailsService(GiangVienRepository giangVienRepository) {
        this.giangVienRepository = giangVienRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        GiangVien giangVien = giangVienRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Tài khoản có email %s không tồn tại", email)));
        return getUserDetails(giangVien);
    }

    private JwtUserDetails getUserDetails(GiangVien giangVien) {
        return new JwtUserDetails(
                giangVien.getHoTen(),
                giangVien.getEmail(),
                giangVien.getPassword(),
                giangVien.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                giangVien.isTrangThai()
        );
    }
}
