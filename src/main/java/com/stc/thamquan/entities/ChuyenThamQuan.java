package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stc.thamquan.entities.embedded.CongTacVienDanDoan;
import com.stc.thamquan.entities.embedded.GPS;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 06:12
 * Filename  : ChuyenThamQuan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chuyen-tham-quan")
public class ChuyenThamQuan {
    @Id
    private String id;

    @ApiModelProperty(value = "Số thứ tự của chuyến thăm quan, tự tăng theo đợt")
    private int thuTu;

    private String tenChuyenThamQuan;

    private String noiDungThamQuan;

    private Date thoiGianKhoiHanh;

    @ApiModelProperty(value = "Id địa điểm bên bảng địa điểm, do admin chỉ định")
    private DiaDiem diaDiemKhoiHanh;

    @ApiModelProperty(value = "Id phương tiện bên bảng phương tiện, do admin chỉ định")
    private List<PhuongTien> phuongTien;

    @DBRef
    private DotThamQuan dotThamQuan;

    @DBRef
    private GiangVien giangVienDangKy;

    @ApiModelProperty(value = "Doanh nghiệp có thể bỏ trống nếu giảng viên chưa có doanh nghiệp, admin có thể đề xuất, tạo mới khi đồng ý")
    @DBRef
    private DoanhNghiep doanhNghiep;

    @ApiModelProperty(value = "Thời gian giảng viên đăng ký tham quan dự kiến, tối thiểu trước thời gian cài đặt trong hệ thống")
    private Date thoiGianDuKien;

    @ApiModelProperty(value = "Để luôn thông tin giảng viên đăng ký nếu giảng viên đăng ký tham gia kết nối doanh nghiệp")
    private List<String> danhSachGiangVienThamGias = new ArrayList<>();

    @ApiModelProperty(value = "Thời gian thăm quan được tạo sau khi liên hệ với công ty, " +
            "nếu giảng viên đã liên hệ trước thì cho chọn thời gian")
    private Date thoiGianBatDauThamQuan;

    @ApiModelProperty(value = "Thời gian kết thúc chuyến tham quan (dự kiến)")
    private Date thoiGianKetThucThamQuan;

    @ApiModelProperty(value = "Danh sách sinh viên chỉ được tạo sau khi admin đã duyệt chuyến tham quan")
    private List<SinhVienThamQuan> danhSachSinhViens = new ArrayList<>();

    @ApiModelProperty(value = "Admin chỉ định danh sách cộng tác viên đi cùng, sau khi đã liên hệ công ty và gửi công văn (nếu có)")
    private List<CongTacVienDanDoan> congTacViens = new ArrayList<>();

    @ApiModelProperty(value = "Admin điều chỉnh khi cần thiết, mặc định lấy từ cấu hình hệ thống")
    private double phuCapCongTacVien;

    @ApiModelProperty(value = "Tổng kinh phí chuyến thăm quan, bao gồm cả thuê xe ngoài nếu có")
    private double kinhPhi;

    @ApiModelProperty(value = "Mặc định là đi xe trường")
    private boolean thueXeNgoaiTruong = false;

    @ApiModelProperty(value = "Id file scan giấy xác nhận doanh nghiệp")
    private String fileScanGiayXacNhanDoanhNghiep;

    @ApiModelProperty(value = "Id file scan kế hoạch")
    private String fileScanKeHoach;

    @ApiModelProperty(value = "Đường dẫn lưu trữ hình ảnh")
    private String duongDan;

    @ApiModelProperty(value = "Id file xác nhận duyệt của ban giám hiệu")
    private String fileXacNhanDuyetTuBanGiamHieu;

    @ApiModelProperty(value="Thời hạn làm khảo sát")
    private Date thoiHanLamKhaoSat;

    @ApiModelProperty(value = "Lý do hủy chuyến")
    private String lyDo;

    @ApiModelProperty(value = "Lấy từ EnumTrangThai")
    private String trangThai;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;

    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;
}
