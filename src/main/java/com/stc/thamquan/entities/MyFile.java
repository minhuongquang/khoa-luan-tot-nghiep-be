package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 08:52
 * Filename  : MyFile
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "my-file")
public class MyFile {
    @Id
    private String id;

    private String tenFile;

    private String duongDan;

    private long kichThuoc;

    private String loaiFile;

    private boolean trangThai = true;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;
}
