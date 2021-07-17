package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 10:00
 * Filename  : KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu {
    private CauHoiKhaoSatDoanhNghiep cauHoi;

    private List<String> cauTraLoi;
}
