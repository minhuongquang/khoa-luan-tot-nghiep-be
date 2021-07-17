package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import com.stc.thamquan.entities.CauHoiKhaoSatSinhVien;
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
 * Filename  : KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu {
    private CauHoiKhaoSatSinhVien cauHoi;

    private List<String> cauTraLoi;
}
