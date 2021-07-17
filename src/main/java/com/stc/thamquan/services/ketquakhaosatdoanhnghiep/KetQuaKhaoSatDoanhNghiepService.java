package com.stc.thamquan.services.ketquakhaosatdoanhnghiep;


import com.stc.thamquan.dtos.ketquakhaosatdoanhnghiep.KetQuaKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.KetQuaKhaoSatDoanhNghiep;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatDoanhNghiepService
 */
public interface KetQuaKhaoSatDoanhNghiepService {
    Page<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsPaging(String search, int page, int size, String sort , String column);

    List<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsActive(String search);

    KetQuaKhaoSatDoanhNghiep getKetQuaKhaoSatDoanhNghiep(String id);

    List<KetQuaKhaoSatDoanhNghiep> createKetQuaKhaoSatDoanhNghiep(KetQuaKhaoSatDoanhNghiepDto dto, Principal principal);

    KetQuaKhaoSatDoanhNghiep updateKetQuaKhaoSatDoanhNghiep(String id, KetQuaKhaoSatDoanhNghiepDto dto, Principal principal);

    KetQuaKhaoSatDoanhNghiep changeStatus(String id);

    boolean checkKhaoSat(String chuyenThamQuanId, Principal principal);

}
