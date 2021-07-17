package com.stc.thamquan.services.cauhinhhethong;

import com.stc.thamquan.dtos.cauhinhhethong.CauHinhHeThongDto;
import com.stc.thamquan.entities.CauHinhHeThong;
import org.springframework.data.domain.Page;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:24
 * Filename  : CauHinhHeThongService
 */
public interface CauHinhHeThongService {
    CauHinhHeThong getCauHinhHeThongCore();

    Page<CauHinhHeThong> getAllCauHinhHeThongPaging(int page, int size, String sort, String column);

    CauHinhHeThong createCauHinhHeThong(CauHinhHeThongDto dto);

    CauHinhHeThong updateCauHinhHeThong(String id, CauHinhHeThongDto dto);

    CauHinhHeThong updatePhuCapCTV(double phuCap);

    double getPhuCapCTV();

    CauHinhHeThong getCauHinhHeThong(String id);
}
