package com.stc.thamquan.utils;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/15/21
 * Time      : 07:46
 * Filename  : EnumTrangThai
 */
public enum EnumTrangThai {
    GIANG_VIEN_CHUA_CO_CONG_TY, // Giảng viên đăng ký chuyến nhưng chưa có công ty để thăm quan
    GIANG_VIEN_DA_CHON_CONG_TY, // Giảng viên đã chọn công ty nhưng chưa liên hệ
    GIANG_VIEN_DA_LIEN_HE_CONG_TY, // Giảng viên đã liên hệ trước với công ty
    DA_LIEN_HE_CONG_TY, // Admin đã liên hệ công ty
    DANG_XU_LY, //
    //DA_DUYET, // Admin xác nhận thông tin chuyến thăm quan với cty
    // CHO_DUYET, // Chờ Ban Giám Hiệu duyệt cho phép đi
    SAN_SANG,   // Đầy đủ thông tin + Ban Giám Hiệu đã duyệt
    HUY, // Chuyến thăm quan bị hủy
    HOAN_TAT    // Kết thúc chuyến tham quan
}
