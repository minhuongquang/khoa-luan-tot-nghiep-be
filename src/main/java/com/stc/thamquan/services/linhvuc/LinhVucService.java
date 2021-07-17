package com.stc.thamquan.services.linhvuc;

import com.stc.thamquan.dtos.linhvuc.LinhVucDto;
import com.stc.thamquan.entities.LinhVuc;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:36
 * Filename  : LinhVucService
 */
public interface LinhVucService {
    Page<LinhVuc> getAllLinhVucPaging(String search, int page, int size, String sort, String column);

    List<LinhVuc> getLinhVucs(String search);

    LinhVuc getLinhVuc(String id);

    LinhVuc getLinhVucByIdCore(String id);

    LinhVuc createLinhVuc(LinhVucDto dto);

    LinhVuc updateLinhVuc(String id, LinhVucDto dto);

    LinhVuc changeStatus(String id);
}
