package com.stc.thamquan.services.excel;

import com.stc.thamquan.dtos.chuyenthamquan.ChuyenThamQuanTinhLuongDto;
import com.stc.thamquan.entities.ChuyenThamQuan;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:48
 * Filename  : ExcelService
 */
public interface ExcelService {

    void importDanhSachSinhVien(ChuyenThamQuan chuyenThamQuan, MultipartFile file) throws Exception;

    void importDanhSachDoanhNghieps(MultipartFile file) throws Exception;

    File exportDanhSachChuyenThamQuanSuDungXeNgoai(List<Map> chuyenThamQuans ) throws  Exception;

    File exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(List<ChuyenThamQuanTinhLuongDto> chuyenThamQuans, String tenDotThamQuan) throws Exception;

    File xuatDanhSachSinhVienThamQuan(ChuyenThamQuan chuyenThamQuan) throws Exception;

    File xuatDanhSachSinhVienVang(ChuyenThamQuan chuyenThamQuan) throws Exception;

    void importDanhSachSinhVienDiemDanhBu(ChuyenThamQuan chuyenThamQuan, MultipartFile file) throws Exception;
}
