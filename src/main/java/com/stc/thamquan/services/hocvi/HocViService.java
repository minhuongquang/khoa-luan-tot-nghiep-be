package com.stc.thamquan.services.hocvi;

import com.stc.thamquan.dtos.hocvi.HocViDto;
import com.stc.thamquan.entities.HocVi;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:39
 * Filename  : HocViService
 */
public interface HocViService {
    Page<HocVi> getAllHocViPaging(String search, int page, int size, String sort, String column);

    List<HocVi> getAllHocVis(String search);

    HocVi getHocVi(String id);

    HocVi getHocViCoreById(String id);

    HocVi createHocVi(HocViDto dto);

    HocVi updateHocVi(String id, HocViDto dto);

    HocVi changeStatus(String id);
}
