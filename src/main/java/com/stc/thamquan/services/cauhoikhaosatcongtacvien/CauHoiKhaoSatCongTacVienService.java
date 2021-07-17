package com.stc.thamquan.services.cauhoikhaosatcongtacvien;

import com.stc.thamquan.dtos.cauhoikhaosatcongtacvien.CauHoiKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatCongTacVien;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CauHoiKhaoSatCongTacVienService {
    Page<CauHoiKhaoSatCongTacVien> getAllCauHoiKhaoSatCongTacViensPaging(String search, int page, int size, String sort , String column);

    List<CauHoiKhaoSatCongTacVien> getAllCauHoiKhaoSatCongTacViensActive(String search, String idChuyenThamQuan);

    CauHoiKhaoSatCongTacVien getCauHoiKhaoSatCongTacVien(String id);

    CauHoiKhaoSatCongTacVien createCauHoiKhaoSatCongTacVien(CauHoiKhaoSatCongTacVienDto dto);

    CauHoiKhaoSatCongTacVien updateCauHoiKhaoSatCongTacVien(String id, CauHoiKhaoSatCongTacVienDto dto);

    CauHoiKhaoSatCongTacVien changeStatus(String id);
}
