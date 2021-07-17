package com.stc.thamquan.securities;

import com.stc.thamquan.entities.DoanhNghiep;
import com.stc.thamquan.repositories.DoanhNghiepRepository;
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
 * Filename  : DoanhNghiepDetailsService
 */
@Slf4j
@Service
public class DoanhNghiepDetailsService implements UserDetailsService {

    private final DoanhNghiepRepository doanhNghiepRepository;

    public DoanhNghiepDetailsService(DoanhNghiepRepository doanhNghiepRepository) {
        this.doanhNghiepRepository = doanhNghiepRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Locale locale = LocaleContextHolder.getLocale();
        DoanhNghiep doanhNghiep = doanhNghiepRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Tài khoản có email %s không tồn tại", email)));
        return getUserDetails(doanhNghiep);
    }

    private JwtUserDetails getUserDetails(DoanhNghiep doanhNghiep) {
        return new JwtUserDetails(
                doanhNghiep.getHoTen(),
                doanhNghiep.getEmail(),
                doanhNghiep.getPassword(),
                doanhNghiep.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                doanhNghiep.isTrangThai()
        );
    }
}
