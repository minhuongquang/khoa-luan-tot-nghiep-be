package com.stc.thamquan.services.cauhoikhaosatsinhvien;


import com.stc.thamquan.dtos.cauhoikhaosatsinhvien.CauHoiKhaoSatSinhVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatSinhVien;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CauHoiKhaoSatSinhVienService {
    Page<CauHoiKhaoSatSinhVien> getAllCauHoiKhaoSatSinhViensPaging(String search, int page, int size, String sort , String column);

    List<CauHoiKhaoSatSinhVien> getAllCauHoiKhaoSatSinhViensActive(String search, String idChuyenThamQuan);

    CauHoiKhaoSatSinhVien getCauHoiKhaoSatSinhVien(String id);

    CauHoiKhaoSatSinhVien createCauHoiKhaoSatSinhVien(CauHoiKhaoSatSinhVienDto dto);

    CauHoiKhaoSatSinhVien updateCauHoiKhaoSatSinhVien(String id, CauHoiKhaoSatSinhVienDto dto);

    CauHoiKhaoSatSinhVien changeStatus(String id);

}
