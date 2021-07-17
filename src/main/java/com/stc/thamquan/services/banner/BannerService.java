package com.stc.thamquan.services.banner;

import com.stc.thamquan.dtos.banner.BannerDto;
import com.stc.thamquan.entities.Banner;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BannerService {
    Page<Banner> getAllBannerPaging(String search, int page, int size, String sort, String column);

    List<Banner> getAllBannerAcive(String search);

    Banner getBanner(String id);

    Banner getBannerByIdCore(String id);

    Banner createBanner(BannerDto dto);

    Banner updateBanner(String id, BannerDto dto);

    Banner changeStatus(String id);

    Banner deleteBanner(String id);
}