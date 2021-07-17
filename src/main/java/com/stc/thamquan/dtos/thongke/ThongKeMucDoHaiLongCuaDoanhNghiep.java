package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.DoanhNghiep;
import com.stc.thamquan.entities.KetQuaKhaoSatDoanhNghiep;
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
 * Filename  : ThongKeMucDoHaiLongCuaDoanhNghiep
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeMucDoHaiLongCuaDoanhNghiep {
    private DoanhNghiep doanhNghiep;

    private ChuyenThamQuan chuyenThamQuan;

    private List<KetQuaKhaoSatDoanhNghiep> ketQuaKhaoSatDoanhNghieps;
}
