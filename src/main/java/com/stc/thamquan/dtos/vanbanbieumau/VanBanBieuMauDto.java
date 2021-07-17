package com.stc.thamquan.dtos.vanbanbieumau;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/11/21
 * Time      : 13:45
 * Filename  : VanBanBieuMauDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VanBanBieuMauDto {
    private String tenVanBanBieuMau;

    private String tenVanBanBieuMauEn;

    private String fileVanBanBieuMau;

    private boolean trangThai;
}
