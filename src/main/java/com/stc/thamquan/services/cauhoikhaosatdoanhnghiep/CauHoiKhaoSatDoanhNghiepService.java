package com.stc.thamquan.services.cauhoikhaosatdoanhnghiep;

import com.stc.thamquan.dtos.cauhoikhaosatdoanhnghiep.CauHoiKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : CauHoiKhaoSatDoanhNghiepService
 */
public interface CauHoiKhaoSatDoanhNghiepService {
    Page<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatDoanhNghiepsPaging(String search, int page, int size, String sort ,String column);

    List<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatDoanhNghiepsActive(String search, String idChuyenThamQuan);

    CauHoiKhaoSatDoanhNghiep getCauHoiKhaoSatDoanhNghiep(String id);

    CauHoiKhaoSatDoanhNghiep createCauHoiKhaoSatDoanhNghiep(CauHoiKhaoSatDoanhNghiepDto dto);

    CauHoiKhaoSatDoanhNghiep updateCauHoiKhaoSatDoanhNghiep(String id, CauHoiKhaoSatDoanhNghiepDto dto);

    CauHoiKhaoSatDoanhNghiep changeStatus(String id);

}
