package com.stc.thamquan.dtos.cauhinhhethong;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauHinhHeThongDto {
    private String emailGuiThu;

    private String passwordEmailGuiThu;

    private double phuCapCongTacVien;

    private String emailNhanThu;
}
