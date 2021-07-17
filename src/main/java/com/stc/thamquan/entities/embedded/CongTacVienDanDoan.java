package com.stc.thamquan.entities.embedded;

import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.entities.TaiKhoan;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 06/11/21
 * Time      : 06:15
 * Filename  : CongTacVienDanDoan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CongTacVienDanDoan {
    private CongTacVien congTacVien;

    private Date ngayDangKy = new LocalDateTime().toDate();

    private Date ngayDuyet;

    private CongTacVien nguoiDangKy;

    private TaiKhoan nguoiDuyet;

    private Boolean trangThai;

    @ApiModelProperty(value = "Id myfile")
    private String fileCheckIn;

    @ApiModelProperty(value = "Id myfile")
    private String fileCheckOut;

    @ApiModelProperty(value = "GPS khi CTV check-in")
    private GPS gpsCheckIn;

    @ApiModelProperty(value = "GPS khi CTV check-out")
    private GPS gpsCheckout;

    @ApiModelProperty(value = "Thời gian cộng tác viên checkin khi đến công ty")
    private Date thoiGianDenCongTy;

    @ApiModelProperty(value = "Thời gian cộng tác viên checkout khi rời công ty")
    private Date thoiGianRoiCongTy;

    public CongTacVienDanDoan(CongTacVien congTacVien, CongTacVien nguoiDangKy) {
        this.congTacVien = congTacVien;
        this.nguoiDangKy = nguoiDangKy;
    }
}
