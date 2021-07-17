package com.stc.thamquan.dtos.chuyenthamquan;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:22
 * Filename  : ChuyenThamQuanDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenThamQuanDto {
    private String tenChuyenThamQuan;

    private Date thoiGianKhoiHanh;

    private String noiDungThamQuan;

    private String diaDiemKhoiHanh;

    @ApiModelProperty(value = "Id phương tiện bên bảng phương tiện")
    private List<String> phuongTien;

    private String dotThamQuan;

    private String doanhNghiep;

    private Date thoiGianBatDauThamQuan;

    private Date thoiGianKetThucThamQuan;

    private List<String> danhSachSinhViens = new ArrayList<>();

    private List<String> congTacViens = new ArrayList<>();

    private double phuCapCongTacVien;

    @ApiModelProperty(value = "Tổng kinh phí chuyến thăm quan, bao gồm cả thuê xe ngoài nếu có")
    private double kinhPhi;

    @ApiModelProperty(value = "Mặc định là đi xe trường")
    private boolean thueXeNgoaiTruong = false;

    private String giangVienDangKy;

}



