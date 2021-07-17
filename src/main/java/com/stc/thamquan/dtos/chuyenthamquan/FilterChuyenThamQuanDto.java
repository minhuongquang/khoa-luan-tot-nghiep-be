package com.stc.thamquan.dtos.chuyenthamquan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:27
 * Filename  : FilterChuyenThamQuanDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterChuyenThamQuanDto {
    private String tenChuyenThamQuan;

    private List<String> trangThai;

    private String namHoc;

    private String hocKy;

    private String dotThamQuan;

    private boolean chuyenMoi;

    private String doanhNghiep;

    private String giangVien;

    private String congTacVien;

    private Date thoiGianBatDau;

    private Date thoiGianKetThuc;

}
