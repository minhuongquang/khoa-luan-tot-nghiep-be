package com.stc.thamquan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/21
 * Time      : 3:53 PM
 * Filename  : Nganh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "nganh")
public class Nganh {
    @Id
    private String id;

    private int thuTu;

    private String maNganh;

    private String tenNganh;

    private boolean trangThai = true;

    @DBRef
    private Khoa khoa;
}
