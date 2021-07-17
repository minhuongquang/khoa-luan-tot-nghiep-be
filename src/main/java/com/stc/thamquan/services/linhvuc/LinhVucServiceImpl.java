package com.stc.thamquan.services.linhvuc;

import com.stc.thamquan.dtos.linhvuc.LinhVucDto;
import com.stc.thamquan.entities.LinhVuc;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.LinhVucRepository;
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
 * Time      : 15:37
 * Filename  : LinhVucServiceImpl
 */
@Slf4j
@Service
public class LinhVucServiceImpl implements LinhVucService {
    private final LinhVucRepository linhVucRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    public LinhVucServiceImpl(LinhVucRepository linhVucRepository,
                              VietnameseStringUtils vietnameseStringUtils) {
        this.linhVucRepository = linhVucRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public Page<LinhVuc> getAllLinhVucPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return linhVucRepository.getAllLinhVucPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<LinhVuc> getLinhVucs(String search) {
        return linhVucRepository.getLinhVucs(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public LinhVuc getLinhVuc(String id) {
        return linhVucRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Lĩnh vực có id %s không tồn tại", id)));
    }

    @Override
    public LinhVuc getLinhVucByIdCore(String id) {
        return linhVucRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public LinhVuc createLinhVuc(LinhVucDto dto) {
        if (ObjectUtils.isEmpty(dto.getMaLinhVuc())) {
            throw new InvalidException("Mã lĩnh vực không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenLinhVuc())) {
            throw new InvalidException("Tên lĩnh vực không được bỏ trống");
        }

        if (linhVucRepository.existsByMaLinhVucIgnoreCase(dto.getMaLinhVuc())) {
            throw new InvalidException(String.format("Mã lĩnh vực %s đã tồn tại", dto.getMaLinhVuc()));
        }
        LinhVuc linhVuc = new LinhVuc();
        linhVuc.setMaLinhVuc(dto.getMaLinhVuc());
        linhVuc.setTenLinhVuc(dto.getTenLinhVuc());
        linhVuc.setTrangThai(true);
        linhVucRepository.save(linhVuc);
        return linhVuc;
    }

    @Override
    public LinhVuc updateLinhVuc(String id, LinhVucDto dto) {
        LinhVuc linhVuc = getLinhVuc(id);
        if (ObjectUtils.isEmpty(dto.getMaLinhVuc())) {
            throw new InvalidException("Mã lĩnh vực không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getTenLinhVuc())) {
            throw new InvalidException("Tên lĩnh vực không được bỏ trống");
        }
        if (!linhVuc.getMaLinhVuc().equalsIgnoreCase(dto.getMaLinhVuc()) &&
                linhVucRepository.existsByMaLinhVucIgnoreCase(dto.getMaLinhVuc())) {
            throw new InvalidException(String.format("Mã lĩnh vực %s đã tồn tại", dto.getMaLinhVuc()));
        }
        linhVuc.setTenLinhVuc(dto.getTenLinhVuc());
        linhVucRepository.save(linhVuc);
        return linhVuc;
    }

    @Override
    public LinhVuc changeStatus(String id) {
        LinhVuc linhVuc = getLinhVuc(id);
        linhVuc.setTrangThai(!linhVuc.isTrangThai());
        linhVucRepository.save(linhVuc);
        return linhVuc;
    }
}
