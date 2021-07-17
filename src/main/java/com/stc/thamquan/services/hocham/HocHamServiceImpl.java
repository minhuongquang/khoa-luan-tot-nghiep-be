package com.stc.thamquan.services.hocham;

import com.stc.thamquan.dtos.hocham.HocHamDto;
import com.stc.thamquan.entities.HocHam;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.HocHamRepository;
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
 * Time      : 14:34
 * Filename  : HocHamServiceImpl
 */
@Slf4j
@Service
public class HocHamServiceImpl implements HocHamService {
    private final HocHamRepository hocHamRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public HocHamServiceImpl(HocHamRepository hocHamRepository, VietnameseStringUtils vietnameseStringUtils) {
        this.hocHamRepository = hocHamRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public Page<HocHam> getAllHocHamPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return hocHamRepository.getAllHocHamPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<HocHam> getAllHocHams(String search) {
        return hocHamRepository.getAllHocHams(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public HocHam getHocHam(String id) {
        return hocHamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Học hàm có id %s không tồn tại", id)));
    }

    @Override
    public HocHam getHocHamByIdCore(String id) {
        return hocHamRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public HocHam createHocHam(HocHamDto dto) {
        if (ObjectUtils.isEmpty(dto.getTenHocHam())) {
            throw new InvalidException("Tên học hàm không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenVietTat())) {
            throw new InvalidException("Tên viết tắt không được bỏ trống");
        }
        if (hocHamRepository.existsByTenVietTatIgnoreCase(dto.getTenVietTat())) {
            throw new InvalidException(String.format("Học hàm có tên viết tắt %s đã tồn tại", dto.getTenVietTat()));
        }
        HocHam hocHam = new HocHam();
        hocHam.setTenHocHam(dto.getTenHocHam());
        hocHam.setTenVietTat(dto.getTenVietTat());
        hocHam.setShow(dto.isShow());
        hocHam.setTrangThai(true);
        hocHamRepository.save(hocHam);
        return hocHam;
    }

    @Override
    public HocHam updateHocHam(String id, HocHamDto dto) {
        HocHam hocHam = getHocHam(id);
        if (ObjectUtils.isEmpty(dto.getTenHocHam())) {
            throw new InvalidException("Tên học hàm không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenVietTat())) {
            throw new InvalidException("Tên viết tắt không được bỏ trống");
        }
        if (!dto.getTenVietTat().equalsIgnoreCase(hocHam.getTenVietTat())
                && hocHamRepository.existsByTenVietTatIgnoreCase(dto.getTenVietTat())) {
            throw new InvalidException(String.format("Học hàm có tên viết tắt %s đã tồn tại", dto.getTenVietTat()));
        }
        hocHam.setTenHocHam(dto.getTenHocHam());
        hocHam.setTenVietTat(dto.getTenVietTat());
        hocHam.setTenVietTat(dto.getTenVietTat());
        hocHam.setShow(dto.isShow());
        hocHamRepository.save(hocHam);
        return hocHam;
    }

    @Override
    public HocHam changeStatus(String id) {
        HocHam hocHam = getHocHam(id);
        hocHam.setTrangThai(!hocHam.isTrangThai());
        hocHamRepository.save(hocHam);
        return hocHam;
    }
}
