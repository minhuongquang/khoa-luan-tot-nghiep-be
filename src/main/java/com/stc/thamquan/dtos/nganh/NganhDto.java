package com.stc.thamquan.dtos.nganh;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/2/21
 * Time      : 11:53
 * Filename  : NganhDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NganhDto {
    private int thuTu;

    private String maNganh;

    private String tenNganh;

    @ApiModelProperty(value = "Id Khoa")
    private String khoa;
}
