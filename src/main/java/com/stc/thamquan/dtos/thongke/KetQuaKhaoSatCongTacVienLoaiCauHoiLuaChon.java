package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.CauHoiKhaoSatCongTacVien;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon {
    private CauHoiKhaoSatCongTacVien cauHoi;

    private List<SoLuotTraLoi> soLuotTraLoiList;
}
