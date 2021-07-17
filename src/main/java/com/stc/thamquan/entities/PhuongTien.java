package com.stc.thamquan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:47
 * Filename  : PhuongTien
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "phuong-tien")
public class PhuongTien {
    @Id
    private String id;

    private int soThuTu;

    private String tenXe;

    private String bienSo;

    private String mauXe;

    private int soChoNgoi;

    private boolean trangThai = true;
}
