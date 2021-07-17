package com.stc.thamquan.services.banner;


import com.stc.thamquan.dtos.banner.BannerDto;
import com.stc.thamquan.entities.Banner;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.BannerRepository;
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
public class BannerServiceImpl implements BannerService {
    private final VietnameseStringUtils vietnameseStringUtils;

    private final BannerRepository bannerRepository;

    private final MessageSource messageSource;

    public BannerServiceImpl(VietnameseStringUtils vietnameseStringUtils, BannerRepository bannerRepository, MessageSource messageSource) {
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.bannerRepository = bannerRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Page<Banner> getAllBannerPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return bannerRepository.getAllBannerPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<Banner> getAllBannerAcive(String search) {
        return bannerRepository.getAllBannerActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public Banner getBanner(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return bannerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(messageSource.getMessage("error.bannernotfound", null, locale), id)));
    }

    @Override
    public Banner getBannerByIdCore(String id)
    {
        return bannerRepository.findByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public Banner createBanner(BannerDto dto) {
        Locale locale = LocaleContextHolder.getLocale();

        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException(messageSource.getMessage("error.bannertieudenotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException(messageSource.getMessage("error.bannerthutunotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getFileBanner())) {
            throw new InvalidException(messageSource.getMessage("error.bannerfilenotempty", null, locale));
        }

        Banner banner = new Banner();
        banner.setTieuDe(dto.getTieuDe());
        banner.setThuTu(dto.getThuTu());
        banner.setFileBanner(dto.getFileBanner());
        banner.setTrangThai(true);

        bannerRepository.save(banner);
        return banner;
    }

    @Override
    public Banner updateBanner(String id, BannerDto dto) {
        Locale locale = LocaleContextHolder.getLocale();
        Banner banner = getBanner(id);

        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException(messageSource.getMessage("error.bannertieudenotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException(messageSource.getMessage("error.bannerthutunotempty", null, locale));
        }

        if (ObjectUtils.isEmpty(dto.getFileBanner())) {
            throw new InvalidException(messageSource.getMessage("error.bannerfilenotempty", null, locale));
        }

        banner.setTieuDe(dto.getTieuDe());
        banner.setThuTu(dto.getThuTu());
        banner.setFileBanner(dto.getFileBanner());

        bannerRepository.save(banner);
        return banner;
    }

    @Override
    public Banner changeStatus(String id) {
        Banner banner = getBanner(id);
        banner.setTrangThai(!banner.isTrangThai());
        bannerRepository.save(banner);
        return banner;
    }

    @Override
    public Banner deleteBanner(String id) {
        Banner banner = getBanner(id);
        bannerRepository.delete(banner);
        return banner;
    }
}
