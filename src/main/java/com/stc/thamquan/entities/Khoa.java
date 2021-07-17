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
 * Date      : 3/29/21
 * Time      : 2:02 PM
 * Filename  : Khoa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "khoa")
public class Khoa {
    @Id
    private String id;

    private int thuTu;

    private String maKhoa;

    private String tenKhoa;
    
    private String tenKhoaEn;

    private boolean trangThai = true;
}
