package com.stc.thamquan.dtos.khoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/2/21
 * Time      : 11:53
 * Filename  : KhoaDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhoaDto {
    private int thuTu;

    private String maKhoa;

    private String tenKhoa;
}
