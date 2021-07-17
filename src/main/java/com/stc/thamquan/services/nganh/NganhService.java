package com.stc.thamquan.services.nganh;

import com.stc.thamquan.dtos.nganh.NganhDto;
import com.stc.thamquan.entities.Nganh;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:44
 * Filename  : NganhService
 */
public interface NganhService {
    Page<Nganh> getNganhPaging(String search, int page, int size, String sort, String column);

    List<Nganh> getAllNganhs(String search);

    List<Nganh> getAllNganhByKhoaId(String khoaId);

    Nganh getNganh(String id);

    Nganh getNganhByIdCore(String id);

    Nganh createNganh(NganhDto dto);

    Nganh updateNganh(String id, NganhDto dto);

    Nganh changeStatus(String id);

}
