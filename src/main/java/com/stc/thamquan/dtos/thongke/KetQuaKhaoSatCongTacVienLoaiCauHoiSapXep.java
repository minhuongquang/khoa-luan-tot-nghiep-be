package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep {
    private CauHoiKhaoSatDoanhNghiep cauHoi;

    private List<CauTraLoi> cauTraLois;
}
