package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:55
 * Filename  : DoanhNghiep
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "doanh-nghiep")
public class DoanhNghiep {
    @Id
    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private String hoTen;

    @ApiModelProperty(value = "Nữ : 1, Nam: 0")
    private boolean gioiTinh;

    private String dienThoai;

    private String congTy;

    private String diaChi;

    private String maSoThue;

    private List<LinhVuc> linhVucs = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

    @ApiModelProperty(value = "% trung bình đánh giá khảo sát của sinh viên sau chuyến tham quan")
    private double diemDanhGiaTrungBinh;

    private boolean trangThai = true;
}
