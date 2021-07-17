package com.stc.thamquan.services.hocvi;

import com.stc.thamquan.dtos.hocvi.HocViDto;
import com.stc.thamquan.entities.HocVi;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.HocViRepository;
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
 * Time      : 14:39
 * Filename  : HocViServiceImpl
 */
@Slf4j
@Service
public class HocViServiceImpl implements HocViService {
    private final HocViRepository hocViRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public HocViServiceImpl(HocViRepository hocViRepository, VietnameseStringUtils vietnameseStringUtils) {
        this.hocViRepository = hocViRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public Page<HocVi> getAllHocViPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return hocViRepository.getAllHocViPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<HocVi> getAllHocVis(String search) {
        return hocViRepository.getAllHocVis(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public HocVi getHocVi(String id) {
        return hocViRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Học vị có id %s không tồn tại", id)));
    }

    @Override
    public HocVi getHocViCoreById(String id) {
        return hocViRepository.getByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public HocVi createHocVi(HocViDto dto) {
        if (ObjectUtils.isEmpty(dto.getTenHocVi())) {
            throw new InvalidException("Tên học vị không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenVietTat())) {
            throw new InvalidException("Tên viết tắt không được bỏ trống");
        }
        if (hocViRepository.existsByTenVietTatIgnoreCase(dto.getTenVietTat())) {
            throw new InvalidException(String.format("Học vị có tên viết tắt %s đã tồn tại", dto.getTenVietTat()));
        }
        HocVi hocVi = new HocVi();
        hocVi.setTenHocVi(dto.getTenHocVi());
        hocVi.setTenVietTat(dto.getTenVietTat());
        hocVi.setTrangThai(true);
        hocViRepository.save(hocVi);
        return hocVi;
    }

    @Override
    public HocVi updateHocVi(String id, HocViDto dto) {
        HocVi hocVi = getHocVi(id);
        if (ObjectUtils.isEmpty(dto.getTenHocVi())) {
            throw new InvalidException("Tên học vị không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenVietTat())) {
            throw new InvalidException("Tên viết tắt không được bỏ trống");
        }
        if (!dto.getTenVietTat().equalsIgnoreCase(hocVi.getTenVietTat()) && hocViRepository.existsByTenVietTatIgnoreCase(dto.getTenVietTat())) {
            throw new InvalidException(String.format("Học vị có tên viết tắt %s đã tồn tại", dto.getTenVietTat()));
        }
        hocVi.setTenHocVi(dto.getTenHocVi());
        hocVi.setTenVietTat(dto.getTenVietTat());
        hocViRepository.save(hocVi);
        return hocVi;
    }


    @Override
    public HocVi changeStatus(String id) {
        HocVi hocVi = getHocVi(id);
        hocVi.setTrangThai(!hocVi.isTrangThai());
        hocViRepository.save(hocVi);
        return hocVi;
    }
}
