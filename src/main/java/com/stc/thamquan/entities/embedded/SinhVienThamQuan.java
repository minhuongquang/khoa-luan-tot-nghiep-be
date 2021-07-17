package com.stc.thamquan.entities.embedded;

import com.stc.thamquan.entities.SinhVien;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 06:15
 * Filename  : SinhVienThamQuan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienThamQuan {
    private SinhVien sinhVien;

    private Date thoiGianDiemDanh;

    @ApiModelProperty(value = "Số phút sinh viên đi trễ, điểm danh sau thời gian bắt đầu tham quan")
    private int soPhutDiTre = 0;

    @ApiModelProperty(value = "")
    private boolean trangThai;

    public SinhVienThamQuan(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
}
