package com.stc.thamquan.dtos.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/8/21
 * Time      : 13:45
 * Filename  : TaiKhoanDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanDto {
    private String email;

    private String maSV;

    private String password;

    private String hoTen;
}
