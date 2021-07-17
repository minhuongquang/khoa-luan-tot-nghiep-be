package com.stc.thamquan.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/11/21
 * Time      : 14:54
 * Filename  : VanBanBieuMau
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "van-ban-bieu-mau")
public class VanBanBieuMau {
    @Id
    private String id;

    private String tenVanBanBieuMau;

    private String tenVanBanBieuMauEn;

    private String fileVanBanBieuMau;

    private boolean trangThai;
}
