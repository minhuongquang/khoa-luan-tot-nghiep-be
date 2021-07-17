package com.stc.thamquan.services.diadiem;

import com.stc.thamquan.dtos.diadiem.DiaDiemDto;
import com.stc.thamquan.entities.DiaDiem;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.DiaDiemRepository;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 18:08
 * Filename  : DiaDiemServiceImpl
 */
@Slf4j
@Service
public class DiaDiemServiceImpl implements DiaDiemService {
    private final DiaDiemRepository diaDiemRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final MessageSource messageSource;

    public DiaDiemServiceImpl(DiaDiemRepository diaDiemRepository, VietnameseStringUtils vietnameseStringUtils, MessageSource messageSource) {
        this.diaDiemRepository = diaDiemRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.messageSource = messageSource;
    }

    @Override
    public Page<DiaDiem> getAllDiaDiemsPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return diaDiemRepository.getAllDiaDiemsPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<DiaDiem> getAllDiaDiemsActive(String search) {
        return diaDiemRepository.getAllDiaDiemsActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public DiaDiem getDiaDiem(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return diaDiemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Địa điểm có id %s không tồn tại", id)));
    }

    @Override
    public DiaDiem getDiaDiemByIdCore(String id) {
        return diaDiemRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public DiaDiem createDiaDiem(DiaDiemDto dto) {
        DiaDiem diaDiem = new DiaDiem();

        if (ObjectUtils.isEmpty(dto.getTenDiaDiem())) {
            throw new InvalidException("Tên địa điểm không được bỏ trống");
        }

        if (diaDiemRepository.existsByTenDiaDiemIgnoreCase(dto.getTenDiaDiem())) {
            throw new InvalidException("Địa điểm đã tồn tại");
        }

        diaDiem.setTenDiaDiem(dto.getTenDiaDiem());
        diaDiemRepository.save(diaDiem);
        return diaDiem;
    }

    @Override
    public DiaDiem updateDiaDiem(String id, DiaDiemDto dto) {
        if (ObjectUtils.isEmpty(dto.getTenDiaDiem())) {
            throw new InvalidException("Tên địa điểm không được bỏ trống");
        }

        if (diaDiemRepository.existsByTenDiaDiemIgnoreCase(dto.getTenDiaDiem())) {
            throw new InvalidException("Địa điểm đã tồn tại");
        }
        DiaDiem diaDiem = getDiaDiem(id);
        diaDiem.setTenDiaDiem(dto.getTenDiaDiem());
        diaDiemRepository.save(diaDiem);
        return diaDiem;
    }

    @Override
    public DiaDiem changeStatus(String id) {
        DiaDiem diaDiem = getDiaDiem(id);
        diaDiem.setTrangThai(!diaDiem.isTrangThai());
        diaDiemRepository.save(diaDiem);
        return diaDiem;
    }


}
