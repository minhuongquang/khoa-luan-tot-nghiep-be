package com.stc.thamquan.services.diadiem;

import com.stc.thamquan.dtos.diadiem.DiaDiemDto;
import com.stc.thamquan.entities.DiaDiem;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 18:08
 * Filename  : DiaDiemService
 */
public interface DiaDiemService {
    Page<DiaDiem> getAllDiaDiemsPaging(String search, int page, int size, String sort, String column );

    List<DiaDiem> getAllDiaDiemsActive(String search);

    DiaDiem getDiaDiem(String id);

    DiaDiem getDiaDiemByIdCore(String id);

    DiaDiem createDiaDiem(DiaDiemDto dto);

    DiaDiem updateDiaDiem(String id, DiaDiemDto dto);

    DiaDiem changeStatus(String id);
}

