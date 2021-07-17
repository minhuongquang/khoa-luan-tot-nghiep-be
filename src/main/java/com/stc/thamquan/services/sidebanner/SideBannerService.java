package com.stc.thamquan.services.sidebanner;

import com.stc.thamquan.dtos.banner.SideBannerDto;
import com.stc.thamquan.entities.SideBanner;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SideBannerService {
    Page<SideBanner> getAllSideBannerPaging(String search, int page, int size, String sort, String column);

    List<SideBanner> getAllSideBannerAcive(String search);

    SideBanner getSideBanner(String id);

    SideBanner getSideBannerByIdCore(String id);

    SideBanner createSideBanner(SideBannerDto dto);

    SideBanner updateSideBanner(String id, SideBannerDto dto);

    SideBanner changeStatus(String id);

    SideBanner deleteSideBanner(String id);
}
