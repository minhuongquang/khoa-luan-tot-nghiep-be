package com.stc.thamquan.services.thongke;

import com.stc.thamquan.dtos.thongke.*;
import com.stc.thamquan.entities.KetQuaKhaoSatDoanhNghiep;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 15:25
 * Filename  : ThongKeService
 */
public interface ThongKeService {
    int thongKeTongChuyenThamQuan(Date tuNgay, Date denNgay);

    List<ThongKeChuyenThamQuanTheoDoanhNghiep> thongKeChuyenThamQuanTheoDoanhNghiep(Date tuNgay, Date denNgay);

    List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId);

    List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiLuaChon(String chuyenThamQuanId);

    List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiSapXep(String chuyenThamQuanId);

    List<KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId);

    List<KetQuaKhaoSatSinhVienLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiLuaChon(String chuyenThamQuanId);

    List<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiSapXep(String chuyenThamQuanId);

    List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> thongKeMucDoHaiLongCuaDoanhNghiep();

    List<KetQuaKhaoSatCongTacVienLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId);

    List<KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiLuaChon(String chuyenThamQuanId);

    List<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiSapXep(String chuyenThamQuanId);


}
