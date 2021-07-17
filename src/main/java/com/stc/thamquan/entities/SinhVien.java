package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/29/21
 * Time      : 2:02 PM
 * Filename  : SinhVien
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sinh-vien")
public class SinhVien {
    @Id
    private String id;

    private String maSV;

    private String cmnd;

    private String hoTen;

    private String email;

    private String lop;

    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "Nữ : 1, Nam: 0")
    private boolean gioiTinh;

    private String dienThoai;

    private Date ngaySinh;

    @DBRef
    private Nganh nganh;

    private List<String> roles = new ArrayList<>();

    private boolean trangThai = true;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;
}
