package com.stc.thamquan.dtos.ketquakhaosatsinhvien;

import com.stc.thamquan.entities.embedded.KetQuaKhaoSat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 13:45
 * Filename  : KetQuaKhaoSatSinhVienDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaKhaoSatSinhVienDto {
    private String chuyenThamQuan;

    private List<KetQuaKhaoSat> ketQuaKhaoSats;

}
