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
@Document(collection = "file-huong-dan")
public class FileHuongDan {
    @Id
    private String id;

    private int thuTu;

    private String tieuDe;

    private String tieuDeEN;

    private String fileHuongDan;

    private boolean trangThai = true;
}
