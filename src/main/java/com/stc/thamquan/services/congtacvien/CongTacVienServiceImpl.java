package com.stc.thamquan.services.congtacvien;

import com.stc.thamquan.dtos.congtacvien.CongTacVienDto;
import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.CongTacVienRepository;
import com.stc.thamquan.utils.EnumRole;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 15:14
 * Filename  : CongTacVienServiceImpl
 */
@Slf4j
@Service
public class CongTacVienServiceImpl implements CongTacVienService {

    private final CongTacVienRepository congTacVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public CongTacVienServiceImpl(CongTacVienRepository congTacVienRepository, VietnameseStringUtils vietnameseStringUtils) {
        this.congTacVienRepository = congTacVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public Page<CongTacVien> getAllCongTacVienPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return congTacVienRepository.getAllCongTacVienPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public CongTacVien getCongTacVien(String id) {
        return congTacVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Cộng tác viên có id %s không tồn tại", id)));
    }

    @Override
    public CongTacVien getCongTacVienByEmail(String email) {
        return congTacVienRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("Cộng tác viên có email %s không tồn tại", email)));
    }

    @Override
    public CongTacVien createCongTacVien(CongTacVienDto dto) {
        CongTacVien congTacVien = new CongTacVien();

        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Họ tên cộng tác viên không được bỏ trống");
        }

        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Điện thoại cộng tác viên không được bỏ trống");
        }

        if (!ObjectUtils.isEmpty(dto.getPassword())) {
            congTacVien.setPassword(dto.getPassword());
        }
        if (!ObjectUtils.isEmpty(dto.getMaSV())) {
            if (congTacVienRepository.existsByMaSV(dto.getMaSV())) {
                throw new InvalidException(String.format("Cộng tác viên có mã sinh viên %s đã tồn tại", dto.getMaSV()));
            }
            congTacVien.setMaSV(dto.getMaSV());
            String emailSV = dto.getMaSV() + "@student.hcmute.edu.vn";
            congTacVien.setEmail(emailSV);
            List<String> roles = Arrays.asList("ROLE_CONG_TAC_VIEN", "ROLE_SINH_VIEN");
            congTacVien.setRoles(roles);

        } else if (!ObjectUtils.isEmpty(dto.getEmail())) {
            congTacVien.setEmail(dto.getEmail());
            congTacVien.setRoles(Collections.singletonList(EnumRole.ROLE_CONG_TAC_VIEN.name()));

        } else {
            throw new InvalidException("Mã sinh viên hoặc email không được để trống.");
        }
        congTacVien.setHoTen(dto.getHoTen());
        congTacVien.setDienThoai(dto.getDienThoai());
        congTacVienRepository.save(congTacVien);
        return congTacVien;
    }

    @Override
    public CongTacVien updateCongTacVien(String id, CongTacVienDto dto) {
        CongTacVien congTacVien = getCongTacVien(id);
        // kiem tra role admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (!ObjectUtils.isEmpty(dto.getMaSV())) {
                if (congTacVienRepository.existsByMaSV(dto.getMaSV()) && !congTacVien.getMaSV().equals(dto.getMaSV())) {
                    throw new InvalidException(String.format("Cộng tác viên có mã sinh viên %s đã tồn tại", dto.getMaSV()));
                }
                congTacVien.setMaSV(dto.getMaSV());
                String emailSV = dto.getMaSV() + "@student.hcmute.edu.vn";
                congTacVien.setEmail(emailSV);
            }
        }
        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Họ tên sinh viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Điện thoại cộng tác viên không được bỏ trống");
        }

        if (!ObjectUtils.isEmpty(dto.getPassword())) {
            congTacVien.setPassword(dto.getPassword());
        }

        if (!ObjectUtils.isEmpty(dto.getEmail())) {
            congTacVien.setEmail(dto.getEmail());
        }
        congTacVien.setHoTen(dto.getHoTen());
        congTacVien.setDienThoai(dto.getDienThoai());
        congTacVienRepository.save(congTacVien);

        return congTacVien;
    }

    @Override
    public CongTacVien changeStatus(String id) {
        CongTacVien congTacVien = getCongTacVien(id);
        congTacVien.setTrangThai(!congTacVien.isTrangThai());
        congTacVienRepository.save(congTacVien);
        return congTacVien;
    }

    @Override
    public CongTacVien getCongTacVienByIdCore(String id) {
        return congTacVienRepository.findByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public CongTacVien getCurrent(Principal principal) {
        return congTacVienRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("Cộng tác viên có email %s không tồn tại", principal.getName())));
    }

    @Override
    public List<CongTacVien> getAllCongTacViensActive(String search) {
        return congTacVienRepository.getAllCongTacViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }
}
