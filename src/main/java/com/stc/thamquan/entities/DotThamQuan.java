package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 06:11
 * Filename  : DotThamQuan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "dot-tham-quan")
public class DotThamQuan {
    @Id
    private String id;

    private String tenDotThamQuan;

    private Date tuNgay;

    private Date denNgay;

    private String namHoc;

    private String hocKy;

    private String mucDich;

    private String yeuCau;

    private String noiDungThamQuan;

    private boolean trangThai;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;

    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;

    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;
}
