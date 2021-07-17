package com.stc.thamquan.securities;

import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.repositories.SinhVienRepository;
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
 * Filename  : SinhVienDetailsService
 */
@Slf4j
@Service
public class SinhVienDetailsService implements UserDetailsService {

    private final SinhVienRepository sinhVienRepository;

    public SinhVienDetailsService(SinhVienRepository sinhVienRepository) {
        this.sinhVienRepository = sinhVienRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!email.contains("@student.hcmute.edu.vn")) {
            throw new InvalidException("Email sử dụng không phải là mail sinh viên");
        }
        SinhVien sinhVien = sinhVienRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Tài khoản có email %s không tồn tại", email)));
        return getUserDetails(sinhVien);
    }

    private JwtUserDetails getUserDetails(SinhVien sinhVien) {
        return new JwtUserDetails(
                sinhVien.getHoTen(),
                sinhVien.getEmail(),
                sinhVien.getPassword(),
                sinhVien.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                sinhVien.isTrangThai()
        );
    }
}
