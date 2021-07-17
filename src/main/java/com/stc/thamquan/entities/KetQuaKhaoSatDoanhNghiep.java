package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 14:54
 * Filename  : KetQuaKhaoSatDoanhNghiep
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ket-qua-khao-sat-doanh-nghiep")

public class KetQuaKhaoSatDoanhNghiep {
    @Id
    private String id;

    @DBRef
    private ChuyenThamQuan chuyenThamQuan;

    @DBRef
    private DoanhNghiep userKhaoSat;

    private CauHoiKhaoSatDoanhNghiep cauHoi;    //Embedded

    private Date thoiGianTraLoi;    // now

    private List<String> cauTraLoi;
    // với câu chọn CHON_MOT hoặc NHAP_LIEU thì là 1 mảng có 1 phần tử
    // với câu SAP_XEP: là mảng đươc sắp xếp theo thứ tự ưu tiên giảm dần (vị trí 0 là ưu tiên)

    private boolean trangThai = true;

}
