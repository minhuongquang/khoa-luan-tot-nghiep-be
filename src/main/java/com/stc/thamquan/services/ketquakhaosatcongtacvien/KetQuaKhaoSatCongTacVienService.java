package com.stc.thamquan.services.ketquakhaosatcongtacvien;

import com.stc.thamquan.dtos.ketquakhaosatcongtacvien.KetQuaKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.KetQuaKhaoSatCongTacVien;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatCongTacVienService
 */
public interface KetQuaKhaoSatCongTacVienService {
    Page<KetQuaKhaoSatCongTacVien> getAllKetQuaKhaoSatCongTacViensPaging(String search, int page, int size, String sort , String column);

    List<KetQuaKhaoSatCongTacVien> getAllKetQuaKhaoSatCongTacViensActive(String search);

    KetQuaKhaoSatCongTacVien getKetQuaKhaoSatCongTacVien(String id);

    List<KetQuaKhaoSatCongTacVien> createKetQuaKhaoSatCongTacVien(KetQuaKhaoSatCongTacVienDto dto, Principal principal);

    KetQuaKhaoSatCongTacVien updateKetQuaKhaoSatCongTacVien(String id, KetQuaKhaoSatCongTacVienDto dto, Principal principal);

    KetQuaKhaoSatCongTacVien changeStatus(String id);

    boolean checkKhaoSat(String chuyenThamQuanId, Principal principal);


}
