package com.stc.thamquan.services.taikhoan;

import com.stc.thamquan.dtos.taikhoan.TaiKhoanDto;
import com.stc.thamquan.entities.TaiKhoan;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.TaiKhoanRepository;
import com.stc.thamquan.utils.EnumRole;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:14
 * Filename  : TaiKhoanServiceImpl
 */
@Slf4j
@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public TaiKhoanServiceImpl(TaiKhoanRepository taiKhoanRepository,
                               VietnameseStringUtils vietnameseStringUtils) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public List<String> getRoleTaiKhoans() {
        return Arrays.stream(EnumRole.values()).map(Enum::name)
                .filter(role -> role.equalsIgnoreCase(EnumRole.ROLE_ADMIN.name()) || role.equalsIgnoreCase(EnumRole.ROLE_CONG_TAC_VIEN.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TaiKhoan> getAllTaiKhoanPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return taiKhoanRepository.getAllTaiKhoanPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public TaiKhoan getTaiKhoan(String id) {
        return taiKhoanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("T??i kho???n c?? id %s kh??ng t???n t???i", id)));
    }

    @Override
    public TaiKhoan getTaiKhoanByEmail(String email) {
        return taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("T??i kho???n c?? email %s kh??ng t???n t???i", email)));
    }

    @Override
    public TaiKhoan createTaiKhoan(TaiKhoanDto dto) {
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new InvalidException("Email kh??ng ???????c b??? tr???ng");
        }
        if (ObjectUtils.isEmpty(dto.getPassword())) {
            throw new InvalidException("Password kh??ng ???????c b??? tr???ng");
        }
        if (taiKhoanRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidException("T??i kho???n c?? email %s ???? t???n t???i");
        }
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setHoTen(dto.getHoTen());
        taiKhoan.setEmail(dto.getEmail());
        taiKhoan.setPassword(dto.getPassword());
        taiKhoan.setRoles(Collections.singletonList(EnumRole.ROLE_ADMIN.name()));
        taiKhoanRepository.save(taiKhoan);
        return taiKhoan;
    }

    @Override
    public TaiKhoan updateTaiKhoan(String id, TaiKhoanDto dto) {
        TaiKhoan taiKhoan = getTaiKhoan(id);
        if (ObjectUtils.isEmpty(dto.getPassword())) {
            throw new InvalidException("Password kh??ng ???????c b??? tr???ng");
        }
        taiKhoan.setHoTen(dto.getHoTen());
        taiKhoan.setPassword(dto.getPassword());
        taiKhoanRepository.save(taiKhoan);
        return taiKhoan;
    }

    @Override
    public TaiKhoan changeStatus(String id) {
        TaiKhoan taiKhoan = getTaiKhoan(id);
        taiKhoan.setTrangThai(!taiKhoan.isTrangThai());
        taiKhoanRepository.save(taiKhoan);
        return taiKhoan;
    }

    @Override
    public TaiKhoan getTaiKhoanByIdCore(String id) {
        return taiKhoanRepository.findById(id).orElse(null);
    }

    @Override
    public TaiKhoan getCurrent(Principal principal) {
        return taiKhoanRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("T??i kho???n c?? email %s kh??ng t???n t???i",principal.getName())));
    }
}
