package com.stc.thamquan.services.phuongtien;

import com.stc.thamquan.dtos.phuongtien.PhuongTienDto;
import com.stc.thamquan.entities.PhuongTien;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 18:07
 * Filename  : PhuongTienService
 */
public interface PhuongTienService {
    Page<PhuongTien> getAllPhuongTiensPaging(String search, int page, int size, String sort, String column );

    List<PhuongTien> getAllPhuongTiensActive(String search);

    PhuongTien getPhuongTien(String id);

    PhuongTien getPhuongTienByIdCore(String id);

    PhuongTien createPhuongTien(PhuongTienDto dto);

    PhuongTien updatePhuongTien(String id, PhuongTienDto dto);

    PhuongTien changeStatus(String id);

}
