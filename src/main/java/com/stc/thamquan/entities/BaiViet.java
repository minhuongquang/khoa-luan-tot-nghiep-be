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

import java.io.Serializable;
import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:19
 * Filename  : BaiViet
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bai-viet")
public class BaiViet implements Serializable {
    @Id
    private String id;

    private int thuTu;

    private String loaiBaiViet;

    private String fileAnhBia;

    private String tieuDe;

    private String tieuDeEn;

    private String noiDung;

    private String noiDungEn;

    private boolean trangThai = true;

    @CreatedDate
    private Date createdDate;

    @JsonIgnore
    @CreatedBy
    private String createdBy;
}
