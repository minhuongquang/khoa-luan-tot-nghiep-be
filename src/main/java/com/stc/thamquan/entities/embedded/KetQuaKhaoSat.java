package com.stc.thamquan.entities.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 06/16/21
 * Time      : 06:15
 * Filename  : KetQuaKhaoSat
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KetQuaKhaoSat {
    private String cauHoiKhaoSat;

    private List<String> cauTraLoi;
}
