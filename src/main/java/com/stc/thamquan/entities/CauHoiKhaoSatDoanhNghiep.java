package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 14:54
 * Filename  : CauHoiKhaoSatDoanhNghiep
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cau-hoi-khao-sat-doanh-nghiep")

public class CauHoiKhaoSatDoanhNghiep {
    @Id
    private String id;

    private int thuTu;

    private String cauHoi;

    private String loaiCauHoi;  // Enum CHON_MOT / CHON_NHIEU / NHAP_LIEU / SAP_XEP

    private int luaChonToiDa;   // Số lựa chọn tối đa đối với câu chọn nhiều, 0 là unlmit

    private List<String> danhSachLuaChon = new ArrayList<>();   // Thứ tự các câu trả lời chính là index của phần tử trong mảng

    private boolean cauHoiBatBuoc = false;

    private boolean trangThai = true;
}
