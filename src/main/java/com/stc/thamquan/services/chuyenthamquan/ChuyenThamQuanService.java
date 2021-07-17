package com.stc.thamquan.services.chuyenthamquan;

import com.stc.thamquan.dtos.chuyenthamquan.*;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.DiaDiem;
import com.stc.thamquan.entities.DotThamQuan;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.entities.embedded.CongTacVienDanDoan;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:19
 * Filename  : ChuyenThamQuanService
 */
public interface ChuyenThamQuanService {
    Page<ChuyenThamQuan> filter(FilterChuyenThamQuanDto dto, int page, int size, String sort, String column);

    Page<ChuyenThamQuan> getChuyenThamQuanPaging(FilterChuyenThamQuanDto dto, int page, int size, String sort, String column);

    List<ChuyenThamQuan> getAllChuyenThamQuansActive(String search);

    ChuyenThamQuan getChuyenThamQuan(String id);

    ChuyenThamQuan createChuyenThamQuan(ChuyenThamQuanDto dto, boolean duyet, Principal principal);

    ChuyenThamQuan updateChuyenThamQuan(String id, ChuyenThamQuanDto dto, Principal principal);

    ChuyenThamQuan deleteChuyenThamQuan(String id);

    void sendMailToDoanhNghiep(String chuyenThamQuanId);

    ChuyenThamQuan adminDuyetChuyenThamQuan(String id, ChuyenThamQuanDto dto, Principal principal);

    ChuyenThamQuan congTacVienCheckInCheckOut(String id, Boolean check);

    ChuyenThamQuan diemDanh(String id, String maSV);

    ChuyenThamQuan ctvCheckInCheckOutAndUploadHinh(String chyenThamQuanId, String typeCheck,CheckinCheckoutDto checkinCheckoutDto, Principal principal);

    void importDanhSachSinhVien(String chuyenThamQuanId, MultipartFile file) throws Exception;

    File xuatCongVanGuiDoanhNghiep(String chuyenThamQuanId) throws Exception;

    ChuyenThamQuan sendMailChuyenThamQuan(String chuyenThamQuanId);

    List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndCongTacVien(Principal principal, Boolean trangThai, String trangThaiChuyen);

    List<ChuyenThamQuan> getAllChuyenThamQuanByDotVaGiangVienThamGia(String idDotThamQuan, Principal principal);

    List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(String idDotThamQuan, Principal principal);

    List<Map> getChuyenThamQuansByThueXeNgoaiTruong(String dotThamQuanId);

    File exportDanhSachChuyenThamQuanSuDungXeNgoai(String dotThamQuanId) throws Exception;

    List<ChuyenThamQuanTinhLuongDto> getDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(String dotThamQuanId);

    // test
    File exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(String dotThamQuanId) throws Exception;
    // test
    ChuyenThamQuan getChuyenThamQuanByThoiGianKhoiHanhAndCongTacVien(Date thoiGianKhoiHanh, String email);
    // test
    File xuatDanhSachSinhVienThamGiaThamQuan(String chuyenThamQuanId) throws Exception;

    String sendCongVanDenCongTyThamQuan(String chuyenThamQuanId) throws Exception;

    // not fine
    Page<ChuyenThamQuan> getChuyenThamQuanPagingChuaCoCongTacVien(String search, int page, int size, String sort, String column, String idDotThamQuan);

    File xuatDanhSachSinhVienVang(String chuyenThamQuanId) throws Exception;

    //not fine
    File xuatKeHoachThamQuan(String idDotThamQuan) throws Exception;

    File xuatPhieuXacNhanSauThamQuan(String chuyenThamQuanId) throws Exception;

    // test lại ctv
    String guiMailTuDong(String id);

    String diemDanhBu(String chuyenThamQuanId, String maSV, int soPhutDiTre);

    ChuyenThamQuan importDanhSachSinhVienDiemDanhBu(String chuyenThamQuanId, MultipartFile file) throws Exception;

    ChuyenThamQuan congTacVienDangKyDanDoan(String idChuyenThamQuan, Principal principal, boolean huy);

    ChuyenThamQuan updateTrangThaiChuyenThamQuan(String id, String trangThai);

    String guiMailCapNhatTrangThai(String id, String lyDo);

    List<ChuyenThamQuan> getChuyenThamQuanActiveByTrangThai();

    List<ChuyenThamQuan> getDuLieuTimeLineGraphic(String trangThai, Date tuNgay, Date denNgay, String idDotThamQuan);

    ChuyenThamQuan luuTruHoSoChuyenThamQuan(String id, LuuTruHoSoDto dto);

    Page<SinhVienThamQuan> getAllSinhVienThamQuansByChuyenThamQuan(String id, String search, int page, int size, String sort, String column);

    ChuyenThamQuan editListSinhViens(String id, List<String> danhSachSinhVien);

    ChuyenThamQuan deleteSinhVienThamQuan(String id, String idSV);

    List<ChuyenThamQuan> getChuyenThamQuansByDotThamQuan(String idDotThamQuan);

    ChuyenThamQuan importFileDuyetTuBGH(String id, MultipartFile file, Principal principal)  throws Exception;

    List<ChuyenThamQuan> createDotThamQuanChoChuyen(RequestChuyenThamQuanDto dto);

    List<ChuyenThamQuan> deleteChuyenThuocDot(String DotThamQuanId, List<String> chuyenThamQuans);

    List<ChuyenThamQuan> updateChuyenVaoDot(String DotThamQuanId, List<String> chuyenThamQuans);

    ChuyenThamQuan adminDuyetCongTacVienDanDoan(String id, String congTacVienId, Boolean duyet, Principal principal);

    ChuyenThamQuan editListCongTacViens(String id, List<String> danhSachCongTacVien, Principal principal);

}
