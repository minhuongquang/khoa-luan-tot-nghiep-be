package com.stc.thamquan.dtos.giangvien;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/8/21
 * Time      : 15:55
 * Filename  : FilterGiangVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FilterGiangVienDto {
    @ApiModelProperty(value = "Tiếm kiếm theo mã giảng viên, email, họ tên giảng viên")
    private String search;

    @ApiModelProperty(value = "Id Khoa")
    private String khoa;

    @ApiModelProperty(value = "Id Ngành")
    private String nganh;

    @ApiModelProperty(value = "Mã lĩnh vực, tên lĩnh vực, tên lĩnh vực tiếng anh")
    private String linhVuc;
}