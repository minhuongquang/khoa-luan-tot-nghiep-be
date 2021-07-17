package com.stc.thamquan.services.ketquakhaosatsinhvien;

import com.stc.thamquan.dtos.ketquakhaosatsinhvien.KetQuaKhaoSatSinhVienDto;
import com.stc.thamquan.entities.KetQuaKhaoSatSinhVien;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatSinhVienService
 */
public interface KetQuaKhaoSatSinhVienService {
    Page<KetQuaKhaoSatSinhVien> getAllKetQuaKhaoSatSinhViensPaging(String search, int page, int size, String sort , String column);

    List<KetQuaKhaoSatSinhVien> getAllKetQuaKhaoSatSinhViensActive(String search);

    KetQuaKhaoSatSinhVien getKetQuaKhaoSatSinhVien(String id);

    List<KetQuaKhaoSatSinhVien> createKetQuaKhaoSatSinhVien(KetQuaKhaoSatSinhVienDto dto, Principal principal);

    KetQuaKhaoSatSinhVien changeStatus(String id);

    boolean checkKhaoSat(String chuyenThamQuanId, Principal principal);
}
