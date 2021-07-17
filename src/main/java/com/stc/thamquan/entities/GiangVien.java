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
 * Time      : 2:03 PM
 * Filename  : GiangVien
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "giang-vien")
public class GiangVien {
    @Id
    private String id;

    private String maGiangVien;

    private String email;

    private String dienThoai;

    @JsonIgnore
    private String password;

    private String hoTen;

    @ApiModelProperty(value = "Nữ : 1, Nam: 0")
    private boolean gioiTinh;

    @DBRef
    private HocHam hocHam;

    @DBRef
    private HocVi hocVi;

    @DBRef
    private Khoa khoa;

    @DBRef
    private Nganh nganh;

    private List<LinhVuc> linhVucs = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

    private boolean thinhGiang = false;

    private boolean trangThai = true;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;

    // Get họ tên giảng viên kèm học hàm học vị viết tắt
    public String getChucDanhKhoaHoc() {
        StringBuilder danhXungGv = new StringBuilder();
        if (this.getHocHam() != null && this.getHocHam().getTenVietTat() != null
                && this.getHocHam().isShow()) {
            danhXungGv.append(this.getHocHam().getTenVietTat());
        }
        if (this.getHocVi() != null && this.getHocVi().getTenVietTat() != null) {
            danhXungGv.append(this.getHocVi().getTenVietTat());
        }
        danhXungGv.append(" ").append(this.getHoTen());
        return danhXungGv.toString().replace("GVCC.", "");
    }
}
