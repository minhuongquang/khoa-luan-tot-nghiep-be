package com.stc.thamquan.dtos.chuyenthamquan;

import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.entities.DoanhNghiep;
import com.stc.thamquan.entities.TaiKhoan;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenThamQuanTinhLuongDto {

    private List<CongTacVien> congTacViens = new ArrayList<>();

    private DoanhNghiep congTy;

    private Date ngayThamQuan;

    private Date thoiGianBatDauThamQuan;

    private Date thoiGianKetThucThamQuan;

    private double tongSoGio;

    @ApiModelProperty(value = "Admin điều chỉnh khi cần thiết, mặc định lấy từ cấu hình hệ thống")
    private double donGia;

    private double thanhTien;

//    private List<TaiKhoan> kyNhan = new ArrayList<>();
}
