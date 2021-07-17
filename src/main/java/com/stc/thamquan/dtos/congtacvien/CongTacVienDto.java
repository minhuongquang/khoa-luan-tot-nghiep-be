package com.stc.thamquan.dtos.congtacvien;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 13:45
 * Filename  : CongTacVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CongTacVienDto {
    private String email;

    private String maSV;

    private String password;

    private String hoTen;

    private String dienThoai;
}
