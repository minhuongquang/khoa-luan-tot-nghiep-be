package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.thongke.*;
import com.stc.thamquan.services.thongke.ThongKeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 15:25
 * Filename  : ThongKeController
 */
@RestController
@RequestMapping("/rest/thong-ke")
@PreAuthorize("hasRole('ADMIN')")
public class ThongKeController {

    private final ThongKeService thongKeService;

    public ThongKeController(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @param tuNgay: ngay bat dau thong ke
     * @param denNgay: ngay ket thuc thong ke
     * @return: so chuyen tham quan trong khoang thoi gian tren
     */
    @ApiOperation(value = "Thống kê số chuyến tham quan trong khoảng thời gian từ ngày ... đến ngày ...")
    @GetMapping("/tong-chuyen-tham-quan")
    public ResponseEntity<Integer> thongKeTongChuyenThamQuan(@RequestParam(name = "tuNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date tuNgay,
                                                             @RequestParam(name = "denNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date denNgay) {
        return new ResponseEntity<>(thongKeService.thongKeTongChuyenThamQuan(tuNgay, denNgay), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: so luong chuyen tham quan theo doanh nghiep
     */
    @ApiOperation(value = "Thống kê số chuyến tham quan theo doanh nghiệp")
    @GetMapping("/so-luong-chuyen-tham-quan-theo-doanh-nghiep")
    public ResponseEntity<List<ThongKeChuyenThamQuanTheoDoanhNghiep>> thongKeChuyenThamQuanTheoDoanhNghiep(@RequestParam(name = "tuNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date tuNgay,
                                                                                                           @RequestParam(name = "denNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date denNgay) {
        return new ResponseEntity<>(thongKeService.thongKeChuyenThamQuanTheoDoanhNghiep(tuNgay, denNgay), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi nhap lieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của doanh nghiệp theo loại câu hỏi nhập liệu của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-doanh-nghiep-theo-loai-cau-hoi-nhap-lieu/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu>> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiNhapLieu(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiNhapLieu(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi chon mot/chon nhieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của doanh nghiệp theo loại câu hỏi chọn một, chọn nhiều của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-doanh-nghiep-theo-loai-cau-hoi-lua-chon/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon>> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiLuaChon(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiLuaChon(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi sap xep
     */
    @ApiOperation(value = "Danh sách câu trả lời của doanh nghiệp theo loại câu hỏi sắp xếp")
    @GetMapping("/danh-sach-cau-tra-loi-doanh-nghiep-theo-loai-cau-hoi-sap-xep/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep>> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiSapXep(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiSapXep(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi nhap lieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của sinh viên theo loại câu hỏi nhập liệu của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-sinh-vien-theo-loai-cau-hoi-nhap-lieu/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu>> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiNhapLieu(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiNhapLieu(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi chon mot/chon nhieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của sinh viên theo loại câu hỏi chọn một, chọn nhiều của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-sinh-vien-theo-loai-cau-hoi-lua-chon/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatSinhVienLoaiCauHoiLuaChon>> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiLuaChon(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiLuaChon(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi sap xep
     */
    @ApiOperation(value = "Danh sách câu trả lời của sinh viên theo loại câu hỏi sắp xếp")
    @GetMapping("/danh-sach-cau-tra-loi-sinh-vien-theo-loai-cau-hoi-sap-xep/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep>> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiSapXep(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiSapXep(chuyenThamQuanId), HttpStatus.OK);
    }
    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi nhap lieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của cộng tác viên theo loại câu hỏi nhập liệu của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-cong-tac-vien-theo-loai-cau-hoi-nhap-lieu/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatCongTacVienLoaiCauHoiNhapLieu>> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiNhapLieu(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiNhapLieu(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi chon mot/chon nhieu
     */
    @ApiOperation(value = "Danh sách câu trả lời của cộng tác viên theo loại câu hỏi chọn một, chọn nhiều của chuyến")
    @GetMapping("/danh-sach-cau-tra-loi-cong-tac-vien-theo-loai-cau-hoi-lua-chon/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon>> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiLuaChon(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiLuaChon(chuyenThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi sap xep
     */
    @ApiOperation(value = "Danh sách câu trả lời của cộng tác viên theo loại câu hỏi sắp xếp")
    @GetMapping("/danh-sach-cau-tra-loi-cong-tac-vien-theo-loai-cau-hoi-sap-xep/{chuyenThamQuanId}")
    public ResponseEntity<List<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep>> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiSapXep(@PathVariable String chuyenThamQuanId) {
        return new ResponseEntity<>(thongKeService.thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiSapXep(chuyenThamQuanId), HttpStatus.OK);
    }
    /***
     * @author: truc
     * @createdDate: 6/17/21
     * @return: danh sach cau tra loi loai cau hoi chon mot/chon nhieu
     */
    @ApiOperation(value = "Thống kê mức độ hài lòng của doanh nghiệp")
    @GetMapping("/thong-ke-muc-do-hai-long-cua-doanh-nghiep")
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon>> thongKeMucDoHaiLongCuaDoanhNghiep() {
        return new ResponseEntity<>(thongKeService.thongKeMucDoHaiLongCuaDoanhNghiep(), HttpStatus.OK);
    }
}
