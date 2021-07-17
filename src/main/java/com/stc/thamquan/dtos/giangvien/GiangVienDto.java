package com.stc.thamquan.dtos.giangvien;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/8/21
 * Time      : 15:43
 * Filename  : GiangVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiangVienDto {
    private String maGiangVien;

    private String email;

    private String hoTen;

    private String dienThoai;

    @ApiModelProperty(value = "Nữ : 1, Nam: 0")
    private boolean gioiTinh;

    @ApiModelProperty(value = "Học hàm id")
    private String hocHam;

    @ApiModelProperty(value = "Học vị id")
    private String hocVi;

    @ApiModelProperty(value = "Khoa id")
    private String khoa;

    @ApiModelProperty(value = "Ngành id")
    private String nganh;

    @ApiModelProperty(value = "Lĩnh vực id")
    private List<String> linhVucs = new ArrayList<>();

    private boolean thinhGiang = false;
}
