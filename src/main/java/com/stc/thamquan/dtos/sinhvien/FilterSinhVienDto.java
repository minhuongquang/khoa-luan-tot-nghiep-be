package com.stc.thamquan.dtos.sinhvien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 13:40
 * Filename  : FilterSinhVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterSinhVienDto {
    private String maSV;

    private String hoTen;

    private String email;

    private String khoa;

    private String nganh;
}
