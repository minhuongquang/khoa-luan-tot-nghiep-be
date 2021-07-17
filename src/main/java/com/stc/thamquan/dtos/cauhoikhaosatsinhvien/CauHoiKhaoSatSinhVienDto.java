package com.stc.thamquan.dtos.cauhoikhaosatsinhvien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEAN
 * User      : truc
 * Date      : 6/4/21
 * Time      : 13:45
 * Filename  : CauHoiKhaoSatSinhVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CauHoiKhaoSatSinhVienDto {
    private int thuTu;

    private String cauHoi;

    private  String loaiCauHoi;

    private int luaChonToiDa;

    private List<String> danhSachLuaChon = new ArrayList<>();

    private boolean cauHoiBatBuoc;

}
