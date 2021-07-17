package com.stc.thamquan.dtos.sinhvien;

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
 * Time      : 13:44
 * Filename  : SinhVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienDto {
    private String maSV;

    private String hoTen;

    private String cmnd;

    private Date ngaySinh;

    private String lop;

    @ApiModelProperty(value = "Nữ : 1, Nam: 0")
    private boolean gioiTinh;

    private String dienThoai;

    @ApiModelProperty(value = "Id ngành")
    private String nganh;

    private String password;
}
