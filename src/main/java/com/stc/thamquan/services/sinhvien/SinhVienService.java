package com.stc.thamquan.services.sinhvien;

import com.stc.thamquan.dtos.sinhvien.FilterSinhVienDto;
import com.stc.thamquan.dtos.sinhvien.SinhVienDto;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 13:37
 * Filename  : SinhVienService
 */
public interface SinhVienService {
    Page<SinhVien> filter(FilterSinhVienDto dto, int page, int size, String sort, String column);

    SinhVien getSinhVien(String id);

    SinhVien getCurrentSinhVien(Principal principal);

    SinhVien getSinhVienByIdCore(String id);

    SinhVien createSinhVien(SinhVienDto dto);

    SinhVien updateSinhVien(String id, SinhVienDto dto);

    SinhVien changeStatus(String id);

    List<SinhVien> getAllSinhViensActive(String search);

}
