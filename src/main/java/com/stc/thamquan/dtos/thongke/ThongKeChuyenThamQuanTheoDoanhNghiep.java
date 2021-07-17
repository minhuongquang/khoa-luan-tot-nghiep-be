package com.stc.thamquan.dtos.thongke;

import com.stc.thamquan.entities.DoanhNghiep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 10:00
 * Filename  : ThongKeChuyenThamQuanTheoDoanhNghiep
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeChuyenThamQuanTheoDoanhNghiep {
    private double soLuong;

    private DoanhNghiep doanhNghiep;

}
