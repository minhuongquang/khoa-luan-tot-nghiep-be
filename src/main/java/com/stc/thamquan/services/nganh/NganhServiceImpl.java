package com.stc.thamquan.services.nganh;

import com.stc.thamquan.dtos.nganh.NganhDto;
import com.stc.thamquan.entities.Nganh;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.NganhReposiroty;
import com.stc.thamquan.services.khoa.KhoaService;
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
 * Time      : 14:44
 * Filename  : NganhServiceImpl
 */
@Slf4j
@Service
public class NganhServiceImpl implements NganhService {
    private final NganhReposiroty nganhReposiroty;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final KhoaService khoaService;

    public NganhServiceImpl(NganhReposiroty nganhReposiroty, VietnameseStringUtils vietnameseStringUtils, KhoaService khoaService) {
        this.nganhReposiroty = nganhReposiroty;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.khoaService = khoaService;
    }

    @Override
    public Page<Nganh> getNganhPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return nganhReposiroty.getAllNganhPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<Nganh> getAllNganhs(String search) {
        return nganhReposiroty.getAllNganhActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public List<Nganh> getAllNganhByKhoaId(String khoaId) {
        return nganhReposiroty.getAllNganhByKhoaId(khoaId);
    }

    @Override
    public Nganh getNganh(String id) {
        return nganhReposiroty.findByIdAndTrangThaiTrue(id)
                .orElseThrow(() -> new NotFoundException(String.format("Ngành có id %s không tồn tại", id)));
    }

    @Override
    public Nganh getNganhByIdCore(String id) {
        return nganhReposiroty.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public Nganh createNganh(NganhDto dto) {
        Nganh nganh = new Nganh();
        if (ObjectUtils.isEmpty(dto.getMaNganh())) {
            throw new InvalidException("Mã ngành không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKhoa())) {
            throw new InvalidException("Khoa không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenNganh())) {
            throw new InvalidException("Tên ngành không được bỏ trống");
        }
        if (nganhReposiroty.existsByMaNganhIgnoreCase(dto.getMaNganh().trim())) {
            throw new InvalidException(String.format("Ngành có mã %s đã tồn tại", dto.getMaNganh()));
        }

        nganh.setKhoa(khoaService.getKhoaByIdCore(dto.getKhoa()));
        nganh.setMaNganh(dto.getMaNganh());
        nganh.setTenNganh(dto.getTenNganh());
        nganh.setThuTu(dto.getThuTu());
        nganhReposiroty.save(nganh);
        return nganh;
    }

    @Override
    public Nganh updateNganh(String id, NganhDto dto) {
        Nganh nganh = getNganh(id);

        if (ObjectUtils.isEmpty(dto.getMaNganh())) {
            throw new InvalidException("Mã ngành không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKhoa())) {
            throw new InvalidException("Khoa không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenNganh())) {
            throw new InvalidException("Tên ngành không được bỏ trống");
        }

        if (!dto.getMaNganh().equalsIgnoreCase(nganh.getMaNganh().trim())
                && nganhReposiroty.existsByMaNganhIgnoreCase(dto.getMaNganh())) {
            throw new InvalidException(String.format("Ngành có mã %s đã tồn tại", dto.getMaNganh()));
        }

        nganh.setKhoa(khoaService.getKhoaByIdCore(dto.getKhoa()));
        nganh.setMaNganh(dto.getMaNganh());
        nganh.setTenNganh(dto.getTenNganh());
        nganh.setThuTu(dto.getThuTu());
        nganhReposiroty.save(nganh);
        return nganh;
    }

    @Override
    public Nganh changeStatus(String id) {
        Nganh nganh = getNganh(id);
        nganh.setTrangThai(!nganh.isTrangThai());
        nganhReposiroty.save(nganh);
        return nganh;
    }


}
