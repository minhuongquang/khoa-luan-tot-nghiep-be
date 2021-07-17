package com.stc.thamquan.services.khoa;

import com.stc.thamquan.dtos.khoa.KhoaDto;
import com.stc.thamquan.entities.Khoa;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:43
 * Filename  : KhoaService
 */
public interface KhoaService {
    Page<Khoa> getKhoaPaging(String search, int page, int size, String sort, String column);

    List<Khoa> getAllKhoas(String search);

    Khoa getKhoa(String id);

    Khoa getKhoaByIdCore(String id);

    Khoa createKhoa(KhoaDto dto);

    Khoa updateKhoa(String id, KhoaDto dto);

    Khoa changeStatus(String id);
}
