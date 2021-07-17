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
 * Time      : 15:48
 * Filename  : DiaDiem
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dia-diem")
public class DiaDiem {
    @Id
    private String id;

    private String tenDiaDiem;

    private boolean trangThai = true;
}
