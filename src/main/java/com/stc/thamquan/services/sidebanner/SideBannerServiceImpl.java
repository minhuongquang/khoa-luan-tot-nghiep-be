package com.stc.thamquan.services.sidebanner;


import com.stc.thamquan.dtos.banner.SideBannerDto;
import com.stc.thamquan.entities.SideBanner;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.SideBannerRepository;
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

@Slf4j
@Service
public class SideBannerServiceImpl implements SideBannerService {
    private final VietnameseStringUtils vietnameseStringUtils;

    private final SideBannerRepository sideBannerRepository;

    private final MessageSource messageSource;

    public SideBannerServiceImpl(VietnameseStringUtils vietnameseStringUtils, SideBannerRepository sideBannerRepository, MessageSource messageSource) {
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.sideBannerRepository = sideBannerRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Page<SideBanner> getAllSideBannerPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return sideBannerRepository.getAllSideBannerPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<SideBanner> getAllSideBannerAcive(String search) {
        return sideBannerRepository.getAllSideBannerActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public SideBanner getSideBanner(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return sideBannerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(messageSource.getMessage("error.sidebannernotfound", null, locale), id)));
    }

    @Override
    public SideBanner getSideBannerByIdCore(String id) {
        return sideBannerRepository.findByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public SideBanner createSideBanner(SideBannerDto dto) {
        Locale locale = LocaleContextHolder.getLocale();

        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannertieudenotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannerthutunotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getFileSideBanner())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannerfilenotempty", null, locale));
        }

        SideBanner sideBanner = new SideBanner();
        sideBanner.setTieuDe(dto.getTieuDe());
        sideBanner.setThuTu(dto.getThuTu());
        sideBanner.setFileSideBanner(dto.getFileSideBanner());
        sideBanner.setTrangThai(true);

        sideBannerRepository.save(sideBanner);
        return sideBanner;
    }

    @Override
    public SideBanner updateSideBanner(String id, SideBannerDto dto) {
        Locale locale = LocaleContextHolder.getLocale();
        SideBanner sideBanner = getSideBanner(id);

        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannertieudenotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannerthutunotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getFileSideBanner())) {
            throw new InvalidException(messageSource.getMessage("error.sidebannerfilenotempty", null, locale));
        }

        sideBanner.setTieuDe(dto.getTieuDe());
        sideBanner.setThuTu(dto.getThuTu());
        sideBanner.setFileSideBanner(dto.getFileSideBanner());

        sideBannerRepository.save(sideBanner);
        return sideBanner;
    }

    @Override
    public SideBanner changeStatus(String id) {
        SideBanner sideBanner = getSideBanner(id);
        sideBanner.setTrangThai(!sideBanner.isTrangThai());
        sideBannerRepository.save(sideBanner);
        return sideBanner;
    }

    @Override
    public SideBanner deleteSideBanner(String id) {
        SideBanner sideBanner = getSideBanner(id);
        sideBannerRepository.delete(sideBanner);
        return sideBanner;
    }
}
