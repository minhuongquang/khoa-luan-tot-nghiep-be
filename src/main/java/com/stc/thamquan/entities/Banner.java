package com.stc.thamquan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "banner")
public class Banner {
    @Id
    private String id;

    private int thuTu;

    private String tieuDe;

    private String fileBanner;

    private boolean trangThai = true;

}
