package com.stc.thamquan.dtos.doanhnghiep;

import com.stc.thamquan.entities.LinhVuc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoanhNghiepDto {
    private String email;

    private String hoTen;

    private boolean gioiTinh;

    private String dienThoai;

    private String congTy;

    private String diaChi;

    private String maSoThue;

    private List<String> linhVucs = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

}
