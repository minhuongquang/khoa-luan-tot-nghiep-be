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
 * Date      : 3/31/21
 * Time      : 15:35
 * Filename  : LinhVuc
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "linh-vuc")
public class LinhVuc {
    @Id
    private String id;

    private String maLinhVuc;

    private String tenLinhVuc;

    private boolean trangThai = true;

    public LinhVuc(String maLinhVuc, String tenLinhVuc) {
        this.maLinhVuc = maLinhVuc;
        this.tenLinhVuc = tenLinhVuc;
    }
}
