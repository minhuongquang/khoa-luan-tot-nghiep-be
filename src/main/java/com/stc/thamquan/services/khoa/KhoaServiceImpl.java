package com.stc.thamquan.services.khoa;

import com.stc.thamquan.dtos.khoa.KhoaDto;
import com.stc.thamquan.entities.Khoa;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.KhoaRepository;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:43
 * Filename  : KhoaServiceImpl
 */
@Slf4j
@Service
public class KhoaServiceImpl implements KhoaService {
    private final KhoaRepository khoaRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public KhoaServiceImpl(KhoaRepository khoaRepository, VietnameseStringUtils vietnameseStringUtils) {
        this.khoaRepository = khoaRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public Page<Khoa> getKhoaPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return khoaRepository.getAllKhoaPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<Khoa> getAllKhoas(String search) {
        return khoaRepository.getAllKhoas(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public Khoa getKhoa(String id) {
        return khoaRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Khoa có id %s không tồn tại", id)));
    }

    @Override
    public Khoa getKhoaByIdCore(String id) {
        return khoaRepository.getByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public Khoa createKhoa(KhoaDto dto) {
        if (ObjectUtils.isEmpty(dto.getMaKhoa())) {
            throw new InvalidException("Mã khoa không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenKhoa())) {
            throw new InvalidException("Tên khoa không được bỏ trống");
        }
        if (khoaRepository.existsByMaKhoaIgnoreCase(dto.getMaKhoa())) {
            throw new InvalidException(String.format("Khoa có mã %s đã tồn tại", dto.getMaKhoa()));
        }
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(dto.getMaKhoa());
        khoa.setTenKhoa(dto.getTenKhoa());
        khoa.setThuTu(dto.getThuTu());
        khoa.setTrangThai(true);
        khoaRepository.save(khoa);
        return khoa;
    }

    @Override
    public Khoa updateKhoa(String id, KhoaDto dto) {
        Khoa khoa = getKhoa(id);
        if (ObjectUtils.isEmpty(dto.getMaKhoa())) {
            throw new InvalidException("Mã khoa không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenKhoa())) {
            throw new InvalidException("Tên khoa không được bỏ trống");
        }
        if (!dto.getMaKhoa().equalsIgnoreCase(khoa.getMaKhoa()) && khoaRepository.existsByMaKhoaIgnoreCase(dto.getMaKhoa())) {
            throw new InvalidException(String.format("Khoa có mã %s đã tồn tại", dto.getMaKhoa()));
        }
        khoa.setMaKhoa(dto.getMaKhoa());
        khoa.setTenKhoa(dto.getTenKhoa());
        khoa.setThuTu(dto.getThuTu());
        khoaRepository.save(khoa);
        return khoa;
    }

    @Override
    public Khoa changeStatus(String id) {
        Khoa khoa = getKhoa(id);
        khoa.setTrangThai(!khoa.isTrangThai());
        khoaRepository.save(khoa);
        return khoa;
    }
}
