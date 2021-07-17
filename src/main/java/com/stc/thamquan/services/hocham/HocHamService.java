package com.stc.thamquan.services.hocham;

import com.stc.thamquan.dtos.hocham.HocHamDto;
import com.stc.thamquan.entities.HocHam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:31
 * Filename  : HocHamService
 */
public interface HocHamService {
    Page<HocHam> getAllHocHamPaging(String search, int page, int size, String sort, String column);

    List<HocHam> getAllHocHams(String search);

    HocHam getHocHam(String id);

    HocHam getHocHamByIdCore(String id);

    HocHam createHocHam(HocHamDto dto);

    HocHam updateHocHam(String id, HocHamDto dto);

    HocHam changeStatus(String id);
}
