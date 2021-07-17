package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.chuyenthamquan.*;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.entities.embedded.CongTacVienDanDoan;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.services.filestore.FileStorageService;
import feign.Param;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:25
 * Filename  : ChuyenThamQuanController
 */
@RestController
@RequestMapping("/rest/chuyen-tham-quan")
public class ChuyenThamQuanController {
    private final ChuyenThamQuanService chuyenThamQuanService;

    private final FileStorageService fileStorageService;

    public ChuyenThamQuanController(ChuyenThamQuanService chuyenThamQuanService, FileStorageService fileStorageService) {
        this.chuyenThamQuanService = chuyenThamQuanService;
        this.fileStorageService = fileStorageService;
    }

    /***
     * @author: duong, hung, truc
     * @createdDate: 10/05/2021
     * @param dto: DTO filter thông tin chuyến tham quan
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách chuyến tham quan đã được phân trang
     */
    @ApiOperation(value = "Get danh sách chuyến tham quan filter theo năm học, học kỳ, đợt, giảng viên, công ty, cộng tác viên, tình trạng (duyệt/không duyệt)")
    @PostMapping("/filter")
    public ResponseEntity<Page<ChuyenThamQuan>> filter(@Valid @RequestBody FilterChuyenThamQuanDto dto,
                                                       @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
                                                       @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
                                                       @RequestParam(name = "column", required = false, defaultValue = "createdDate") String column) {

        return new ResponseEntity<>(chuyenThamQuanService.filter(dto, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 28/05/2021
     * @param dto: DTO filter thông tin chuyến tham quan
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách chuyến tham quan đã được phân trang
     */
    @ApiOperation(value = "GET paging DS chuyến tham quan dùng chung cho user")
    @GetMapping("/paging-chuyen-tham-quan")
    public ResponseEntity<Page<ChuyenThamQuan>> getChuyenThamQuanPaging(
            @Valid @RequestBody FilterChuyenThamQuanDto dto,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenDoanhNghiep") String column) {
        return new ResponseEntity<>(chuyenThamQuanService.getChuyenThamQuanPaging(dto, page, size, sort, column), HttpStatus.OK);
    }


    @ApiOperation(value = "Get chuyến tham quan by id")
    @GetMapping("/{id}")
    public ResponseEntity<ChuyenThamQuan> getChuyenThamQuan(@PathVariable String id) {
        return new ResponseEntity<>(chuyenThamQuanService.getChuyenThamQuan(id), HttpStatus.OK);
    }

    /***
     * @author: hung, truc
     * @createdDate: 28/04/2021
     * @param principal: token của cộng tác viên đăng login vào hệ thống
     * @param: id đợt tham quan (nếu null, get theo all đợt active
     * @return: danh sách chuyến tham quan ctv đã đky, được duyệt
     */
    @ApiOperation(value = "Get danh sách chuyến tham quan ctv đã đky, được duyệt")
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @GetMapping("/dot-active-cua-cong-tac-vien")
    public ResponseEntity<List<ChuyenThamQuan>> getAllChuyenThamQuanByDotActiveAndCongTacVien(Principal principal,
                                                                                              @RequestParam(name = "trangThai", required = false) Boolean trangThai,
                                                                                              @RequestParam(name = "trangThaiChuyen", required = true) String trangThaiChuyen) {
        return new ResponseEntity<>(chuyenThamQuanService.getAllChuyenThamQuanByDotActiveAndCongTacVien(principal, trangThai, trangThaiChuyen), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 27/04/2021
     * @return: messages
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Import danh sách sinh viên")
    @PostMapping("/{id}")
    public ResponseEntity<String> importDanhSachSinhVien(@PathVariable String id,
                                                         @RequestParam(name = "file") MultipartFile file) throws Exception {
        chuyenThamQuanService.importDanhSachSinhVien(id, file);
        return new ResponseEntity<>("Import danh sách sinh viên thành công", HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 20/04/2021
     * @return: messages
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/gui-mail-doanh-nghiep")
    public ResponseEntity<String> sendMailToDoanhNghiep(@PathVariable String id) throws Exception {
        chuyenThamQuanService.sendMailToDoanhNghiep(id);
        return new ResponseEntity<>("Gửi mail thông báo cho doanh nghiệp thành công", HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createdDate: 23/04/2021
     * @param id: id của chuyến tham quan admin duyệt
     * @param dto : DTO chuyến tham quan admin muốn cập nhật lại thông tin
     * @return: chuyến tham quan đã duyệt và cập nhật thông tin cần thiết
     */
    @ApiOperation(value = "Admin duyệt chuyến thăm quan, có thể cập nhật thông tin nếu cần thiết")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin-duyet")
    public ResponseEntity<ChuyenThamQuan> adminDuyetChuyenThamQuan(@PathVariable String id, @RequestBody ChuyenThamQuanDto dto, Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.adminDuyetChuyenThamQuan(id, dto, principal), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createdDate: 27/04/2021
     * @param
     * @return: Resource
     */
    @ApiOperation(value = "Admin xuất công văn gửi công ty xin tham quan")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin-xuat-cong-van-xin-tham-quan")
    public ResponseEntity<Resource> adminXuatCongVanXinThamQuan(@PathVariable String id) throws Exception {
        File file = chuyenThamQuanService.xuatCongVanGuiDoanhNghiep(id);
        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: hung
     * @createdDate: 28/04/2021
     * @param id: id chuyến tham quan
     * @param checkinCheckoutDto
     * @return: chuyến tham quan
     */
    @ApiOperation(value = "Cộng tác viên check-in thời gian đến công ty, check-out thời gian rời công ty (upload hình minh chứng)")
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @PostMapping("/{id}/ctv-check-in-check-out-upload-hinh/{typeCheck}")
    public ResponseEntity<ChuyenThamQuan> ctvCheckInCheckOutAndUploadHinh(@PathVariable String id,
                                                                          @PathVariable String typeCheck,
                                                                          @Valid @RequestBody CheckinCheckoutDto checkinCheckoutDto,
                                                                          Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.ctvCheckInCheckOutAndUploadHinh(id, typeCheck, checkinCheckoutDto, principal), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GIANG_VIEN','THU_KY_KHOA')")
    @ApiOperation(value = "Create chuyến tham quan")
    @PostMapping("/create-chuyen-tham-quan")
    public ResponseEntity<ChuyenThamQuan> dangKyChuyenThamQuan(@Valid @RequestBody ChuyenThamQuanDto dto,
                                                               @RequestParam(name = "duyet", required = false, defaultValue = "false") boolean duyet,
                                                               Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.createChuyenThamQuan(dto, duyet, principal), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GIANG_VIEN')")
    @ApiOperation(value = "Update chuyến tham quan")
    @PutMapping("/{id}")
    public ResponseEntity<ChuyenThamQuan> updateChuyenThamQuan(@PathVariable String id,
                                                               @Valid @RequestBody ChuyenThamQuanDto dto,
                                                               Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.updateChuyenThamQuan(id, dto, principal), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA','GIANG_VIEN')")
    @ApiOperation(value = "Delete chuyến tham quan")
    @DeleteMapping("/{id}")
    public ResponseEntity<ChuyenThamQuan> deleteChuyenThamQuan(@PathVariable String id) {
        return new ResponseEntity<>(chuyenThamQuanService.deleteChuyenThamQuan(id), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createdDate: 27/04/2021
     * @param id: id của chuyến tham quan
     * @return: chuyến tham quan đã send mail
     */
    @ApiOperation(value = "Send email cho giảng viên về chuyến tham quan")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @PostMapping("/{id}/send-mail")
    public ResponseEntity<ChuyenThamQuan> sendMailGiangVien(@PathVariable String id) {
        return new ResponseEntity<>(chuyenThamQuanService.sendMailChuyenThamQuan(id), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 28/04/2021
     * @param id: id chuyến tham quan
     * @param check: trạng thái checkin(true), checkout(false)
     * @return: chuyến tham quan đã được cộng tác viên checkin và checkout
     */
    @ApiOperation(value = "Cộng tác viên checkin thời gian đến công ty, checkout thời gian rời công ty")
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @PostMapping("/cong-tac-vien/checkin-checkout/{id}/{check}")
    public ResponseEntity<ChuyenThamQuan> congTacVienCheckInCheckOutThoiGian(@PathVariable String id, @PathVariable Boolean check) {
        return new ResponseEntity<>(chuyenThamQuanService.congTacVienCheckInCheckOut(id, check), HttpStatus.OK);
    }

    /**
     * @param id:   id của chuyến tham quan
     * @param maSV: MSSV của sinh viên
     * @author: ntquoc
     * @createdDate: 28/04/2021
     * @return: Chuyến tham quan
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CONG_TAC_VIEN')")
    @ApiOperation(value = "Điểm danh sinh viên trong chuyến tham quan")
    @PutMapping("/diem-danh/{id}")
    public ResponseEntity<ChuyenThamQuan> diemDanh(@PathVariable String id, @RequestParam String maSV) {
        return new ResponseEntity<>(chuyenThamQuanService.diemDanh(id, maSV), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createdDate: 28/04/2021
     * @return: List chuyến tham quan theo đợt mà giảng viên có tham gia
     */

    @ApiOperation(value = "Lấy danh sách chuyến tham quan mà giảng viên tham gia")
    @PreAuthorize("hasRole('GIANG_VIEN')")
    @GetMapping("/dot-active-cua-giang-vien")
    public ResponseEntity<List<ChuyenThamQuan>> getAllChuyenThamQuanByDotVaGiangVienThamGia(Principal principal,
                                                                                            @RequestParam(name = "idDotThamQuan", required = false) String idDotThamQuan) {
        return new ResponseEntity<>(chuyenThamQuanService.getAllChuyenThamQuanByDotVaGiangVienThamGia(idDotThamQuan, principal), HttpStatus.OK);

    }

    /***
     * @author: truc
     * @createdDate: 27/04/2021
     * @Param:
     * @return: list chuyến tham quan by đợt active sinh viên
     */
    @PreAuthorize("hasRole('SINH_VIEN')")
    @ApiOperation(value = "Get danh sách chuyến tham quan theo đợt active hiện tại của sinh viên đăng nhập")
    @GetMapping("/dot-active-cua-sinh-vien")
    public ResponseEntity<List<ChuyenThamQuan>> getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(Principal principal,
                                                                                                  @RequestParam(name = "idDotThamQuan", required = false) String idDotThamQuan) {
        return new ResponseEntity<>(chuyenThamQuanService.getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(idDotThamQuan, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 10/05/2021
     * @Param: dotThamQuanId
     * @return: list chuyến tham quan
     */
    @ApiOperation(value = "Thống kê chuyến tham quan thuê xe ngoài trường theo đợt tham quan")
    @GetMapping("/thong-ke-chuyen-tham-quan-theo-xe-ngoai-truong/{dotThamQuanId}")
    public ResponseEntity<List<Map>> getChuyenThamQuansByThueXeNgoaiTruong(@PathVariable String dotThamQuanId) {
        return new ResponseEntity(chuyenThamQuanService.getChuyenThamQuansByThueXeNgoaiTruong(dotThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 10-05-2021
     * @param: id đợt tham quan
     * @return: file
     */
    @ApiOperation(value = "Report excel danh sách đề nghị sử dụng xe ngoài")
    @GetMapping("/export-danh-sach-chuyen-tham-quan-su-dung-xe-ngoai/{dotThamQuanId}")
    public ResponseEntity<Resource> exportDanhSachChuyenThamQuanSuDungXeNgoai(@PathVariable String dotThamQuanId) throws Exception {
        File file = chuyenThamQuanService.exportDanhSachChuyenThamQuanSuDungXeNgoai(dotThamQuanId);
        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: ntquoc
     * @createdDate: 15/05/2021
     * @Param: dotThamQuanId
     * @return: list chuyến tham quan
     */
    @ApiOperation(value = "Tính lương cho cộng tác viên theo khoảng thời gian")
    @GetMapping("/tinh-luong-cong-tac-vien/")
    public ResponseEntity<List<ChuyenThamQuanTinhLuongDto>> getDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(@RequestParam String dotThamQuanId) {
        return new ResponseEntity(chuyenThamQuanService.getDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(dotThamQuanId), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 15-05-2021
     * @param: id đợt tham quan
     * @return: file
     */
    @ApiOperation(value = "Report excel lương CTV")
    @GetMapping("/export-danh-sach-luong-cong-tac-vien/")
    public ResponseEntity<Resource> exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(@RequestParam String dotThamQuanId) throws Exception {
        File file = chuyenThamQuanService.exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(dotThamQuanId);
        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: vy
     * @createdDate: 06/05/2021
     * @param :
     * @return: danh sách sinh viên tham gia tham quan không đi trể
     */
    @ApiOperation(value = "Xuất danh sách sinh viên tham gia chuyến tham quan by id")
    @PreAuthorize("hasRole('GIANG_VIEN')")
    @GetMapping("/xuat-sinh-vien-tham-gia/{id}")
    public ResponseEntity<Resource> xuatDanhSanhSinhVienThamQuan(@PathVariable String id) throws Exception {
        File file = chuyenThamQuanService.xuatDanhSachSinhVienThamGiaThamQuan(id);

        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: duong
     * @createdDate: 19-05-2021
     * @param: id  chuyến tham quan
     * @return danh sách sinh viên vắng mặt
     */
    @ApiOperation(value = "Xuất danh sách sinh viên vắng by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'GIANG_VIEN')")
    @GetMapping("/xuat-sinh-vien-vang/{id}")
    public ResponseEntity<Resource> xuatDanhSachSinhVienVang(@PathVariable String id) throws Exception {
        File file = chuyenThamQuanService.xuatDanhSachSinhVienVang(id);
        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: truc
     * @createDate: 10-05-2021
     * @param: id chuyến tham quan
     * @return: String
     */
    @ApiOperation(value = "Gửi công văn đến công ty tham quan")
    @PostMapping("/{id}/gui-cong-van-den-cong-ty-tham-quan")
    public ResponseEntity<String> sendCongVanDenCongTyThamQuan(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(chuyenThamQuanService.sendCongVanDenCongTyThamQuan(id), HttpStatus.OK);
    }

    /***
     * @author: tvtuan
     * @createdDate: 11/05/2021
     * @param search: chuyen tham quan chua co cong tac vien
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Chuyến tham quan
     */
//    @PreAuthorize("hasAnyRole('ADMIN', 'CONG_TAC_VIEN', 'GIANG_VIEN', 'DOANH_NGHIEP', 'SINH_VIEN')")
    @ApiOperation(value = "Get danh sách chuyến tham quan chưa có cộng tác viên của đợt hiện tại (phân trang)")
    @GetMapping("/paging")
    public ResponseEntity<Page<ChuyenThamQuan>> getChuyenThamQuanPagingChuaCoCongTacVien(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenHocHam") String column,
            @RequestParam(name = "idDotThamQuan", required = true, defaultValue = "") String idDotThamQuan) {
        return new ResponseEntity<>(chuyenThamQuanService.getChuyenThamQuanPagingChuaCoCongTacVien(search, page, size, sort, column, idDotThamQuan), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createdDate: 19/05/2021
     * @param
     * @return: Resource
     */
    @ApiOperation(value = "Admin kế hoạch tham quan")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/xuat-ke-hoach-tham-quan")
    public ResponseEntity<Resource> xuatKeHoachThamQuan(@RequestParam(name = "idDotThamQuan", required = true, defaultValue = "") String idDotThamQuan) throws Exception {
        File file = chuyenThamQuanService.xuatKeHoachThamQuan(idDotThamQuan);
        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: truc
     * @createdDate: 14/05/2021
     * @Param: id chuyến tham quan
     * @return: thông báo thành công
     */
    @ApiOperation(value = "Nhắc mail tự động")
    @PostMapping("/{id}/nhac-mail-tu-dong")
    public ResponseEntity<String> guiMailTuDong(@PathVariable String id) {
        return new ResponseEntity<>(chuyenThamQuanService.guiMailTuDong(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 19/05/2021
     * @Param: id chuyến tham quan, mã sinh viên
     * @return: chuyến tham quan
     */
    @ApiOperation(value = "Admin điểm danh bù cho sinh viên")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/diem-danh-bu")
    public ResponseEntity<String> diemDanhBu(@PathVariable String id,
                                             @RequestParam(name = "maSV", required = true) String maSV,
                                             @RequestParam(name = "soPhutDiTre", required = true) int soPhutDiTre) {
        return new ResponseEntity<>(chuyenThamQuanService.diemDanhBu(id, maSV, soPhutDiTre), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 19/05/2021
     * @Param: id chuyến tham quan, file excel danh sách sinh viên điểm danh bù
     * @return: chuyến tham quan
     */
    @ApiOperation(value = "Admin điểm danh bù cho sinh viên bằng file excel")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/diem-danh-bu-bang-file-excel")
    public ResponseEntity<ChuyenThamQuan> diemDanhBu(@PathVariable String id,
                                                     @RequestParam(name = "file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(chuyenThamQuanService.importDanhSachSinhVienDiemDanhBu(id, file), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createdDate: 19/05/2021
     * @param chuyenThamQuanId id của chuyến tham quan
     * @return: file
     */
    @ApiOperation(value = "Xuất report giấy xác nhận sau chuyến tham quan")
    @GetMapping("/xuat-phieu-xac-nhan-tham-quan/{chuyenThamQuanId}")
    public ResponseEntity<Resource> xuatPhieuXacNhanThamQuan(@PathVariable String chuyenThamQuanId) throws Exception {
        File file = chuyenThamQuanService.xuatPhieuXacNhanSauThamQuan(chuyenThamQuanId);

        Resource resource = fileStorageService.loadFile(file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("filename", file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    /***
     * @author: truc
     * @createdDate: 26/05/2021
     * @Param: id chuyến tham quan
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @ApiOperation(value = "Cộng tác viên đăng ký/ hủy đăng ký dẫn đoàn trước khi admin duyệt")
    @PostMapping("/{id}/ctv-dang-ky-dan-doan")
    public ResponseEntity<ChuyenThamQuan> congTacVienDangKyDanDoan(@PathVariable String id,
                                                                   Principal principal,
                                                                   @RequestParam(name = "huy", required = true, defaultValue = "false") boolean huy) {
        return new ResponseEntity<>(chuyenThamQuanService.congTacVienDangKyDanDoan(id, principal, huy), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 11/06/2021
     * @Param: id chuyến tham quan
     * @param: duyệt hay không duyệt
     * @Param: id cộng tác viên
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Admin duyệt cộng tác viên đăng ký dẫn đoàn")
    @PostMapping("/{id}/admin-duyet-ctv-dang-ky-dan-doan")
    public ResponseEntity<ChuyenThamQuan> adminDuyetCongTacVienDanDoan(@PathVariable String id,
                                                                       @RequestParam(name = "congTacVienId", required = true) String congTacVienId,
                                                                       @RequestParam(name = "duyet", required = true) Boolean duyet,
                                                                       Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.adminDuyetCongTacVienDanDoan(id, congTacVienId, duyet, principal), HttpStatus.OK);
    }

    /***
     *
     * @author: truc
     * @createdDate: 27/05/2021
     * @Param: id chuyến tham quan, trạng thái
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Admin cập nhật trạng thái chuyến tham quan: hủy (cần truyền lý do), đang xử lý, Sẵn sàng")
    @PutMapping("/{id}/admin-cap-nhat-trang-thai")
    public ResponseEntity<ChuyenThamQuan> updateTrangThaiChuyenThamQuan(@PathVariable String id,
                                                                        @RequestParam(name = "trangThai", required = true) String trangThai) {
        return new ResponseEntity<>(chuyenThamQuanService.updateTrangThaiChuyenThamQuan(id, trangThai), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 27/05/2021
     * @Param: id chuyến tham quan
     * @return: string
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Gửi mail thông báo đến GV đăng ký khi đã thay đổi trạng thái chuyến tham quan")
    @PostMapping("/{id}/gui-mail-thong-bao-cap-nhat-trang-thai")
    public ResponseEntity<String> guiMailCapNhatTrangThai(@PathVariable String id,
                                                          @RequestParam(name = "lyDo", required = true, defaultValue = "") String lyDo) {
        return new ResponseEntity<>(chuyenThamQuanService.guiMailCapNhatTrangThai(id, lyDo), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 28/05/2021
     * @return: danh sách chuyến tham quan
     */
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @ApiOperation(value = "Get chuyến tham quan active by trạng thái DA_DUYET, DANG_XU_LY để CTV đăng ký dẫn đoàn")
    @GetMapping("/chuyen-tham-quan-active-cho-ctv")
    public ResponseEntity<List<ChuyenThamQuan>> getChuyenThamQuanActiveByTrangThai() {
        return new ResponseEntity<>(chuyenThamQuanService.getChuyenThamQuanActiveByTrangThai(), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 31/05/2021
     * @param:
     * @return: danh sách chuyến tham quan
     */
    @ApiOperation(value = "Get list chuyến tham quan để show lên timeline")
    @GetMapping("/chuyen-tham-quan-len-timeline")
    public ResponseEntity<List<ChuyenThamQuan>> getDuLieuTimeLineGraphic(@RequestParam(name = "trangThai", required = true) String trangThai,
                                                                         @RequestParam(name = "tuNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date tuNgay,
                                                                         @RequestParam(name = "denNgay", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date denNgay,
                                                                         @RequestParam(name = "idDotThamQuan", required = false) String idDotThamQuan) {
        return new ResponseEntity<>(chuyenThamQuanService.getDuLieuTimeLineGraphic(trangThai, tuNgay, denNgay, idDotThamQuan), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 05/06/2021
     * @Param: id chuyến tham quan
     * @Param: principal
     * @Param: File PDF Scan Giấy Xác nhận
     * @Param: Đường Dẫn Thư Mục
     * @Param: File PDF Scan Kế Hoạch
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Lưu trữ chuyến tham quan")
    @PostMapping("/{id}/luu-tru-chuyen-tham-quan")
    public ResponseEntity<ChuyenThamQuan> luuTruHoSoChuyenThamQuan(@PathVariable String id,
                                                                   @Valid @RequestBody LuuTruHoSoDto dto) {
        return new ResponseEntity<>(chuyenThamQuanService.luuTruHoSoChuyenThamQuan(id, dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 31/05/2021
     * @param: id chuyến tham quan
     * @return: danh sách sinh viên tham gia
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'CONG_TAC_VIEN')")
    @ApiOperation(value = "Get paging danh sách sinh viên by ID chuyến tham quan, search theo mã SV, họ tên, lớp, điện thoại")
    @GetMapping("/{id}/paging-danh-sach-sinh-vien-tham-quan")
    public ResponseEntity<Page<SinhVienThamQuan>> getAllSinhViensByChuyenThamQuan(@PathVariable String id,
                                                                                  @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                                  @RequestParam(name = "page", required = false, defaultValue = " ${paging.default.page}") int page,
                                                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                                                  @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
                                                                                  @RequestParam(name = "column", required = false, defaultValue = "sinhVien.maSV") String column) {
        return new ResponseEntity<>(chuyenThamQuanService.getAllSinhVienThamQuansByChuyenThamQuan(id, search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/06/2021
     * @param: id chuyến tham quan
     * @param: danh sách sinh viên tham gia
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Edit List SV by ID chuyến tham quan")
    @PostMapping("/{id}/chinh-sua-danh-sach-sinh-vien")
    public ResponseEntity<ChuyenThamQuan> editListSinhViens(@PathVariable String id,
                                                            @Valid @RequestBody List<String> danhSachSinhVien) {
        return new ResponseEntity<>(chuyenThamQuanService.editListSinhViens(id, danhSachSinhVien), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/08/2021
     * @param: id chuyến tham quan
     * @param: idSV muốn xóa
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Xóa sinh viên trong danh sách sinh viên tham quan ")
    @DeleteMapping("/{id}/xoa-sinh-vien")
    public ResponseEntity<ChuyenThamQuan> deleteSinhVienThamQuan(@PathVariable String id,
                                                                 @Valid @RequestParam(name = "idSV", required = true, defaultValue = "") String idSV) {
        return new ResponseEntity<>(chuyenThamQuanService.deleteSinhVienThamQuan(id, idSV), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/08/2021
     * @param: id đợt tham quan
     * @return: danh sách chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get list chuyến tham quan by đợt tham quan")
    @GetMapping("/danh-sach-chuyen-tham-by-dot-tham-quan/{idDotThamQuan}")
    public ResponseEntity<List<ChuyenThamQuan>> getChuyenThamQuansByDotThamQuan(@PathVariable String idDotThamQuan) {
        return new ResponseEntity<>(chuyenThamQuanService.getChuyenThamQuansByDotThamQuan(idDotThamQuan), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/10/2021
     * @param: id chuyến tham quan
     * @param: file xác nhận duyệt từ ban giám hiệu
     * @return: chuyến tham quan sẵn sàng
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Import file xác nhận duyệt từ ban giám hiệu")
    @PostMapping("/{id}/import-file-xac-nhan-ban-giam-hieu-duyet")
    public ResponseEntity<ChuyenThamQuan> importFileDuyetTuBGH(@PathVariable String id,
                                                               @RequestParam(name = "file", required = true) MultipartFile file,
                                                               Principal principal) throws Exception {
        return new ResponseEntity<>(chuyenThamQuanService.importFileDuyetTuBGH(id, file, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/10/2021
     * @param: dto đợt tham quan
     * @param: list string id chuyến
     * @return: list chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "API CREATE đợt tham quan cho chuyến đi")
    @PostMapping("/create-dot-tham-quan-cho-chuyen-di")
    public ResponseEntity<List<ChuyenThamQuan>> createDotThamQuanChoChuyen(@Valid @RequestBody RequestChuyenThamQuanDto dto) {
        return new ResponseEntity<>(chuyenThamQuanService.createDotThamQuanChoChuyen(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/10/2021
     * @param: id đợt
     * @param: list string id chuyến muốn xóa
     * @return: list chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "API Delete chuyến trong đợt")
    @DeleteMapping("/delete-chuyen-tham-quan-thuoc-dot/{DotThamQuanId}")
    public ResponseEntity<List<ChuyenThamQuan>> deleteChuyenThuocDot(@PathVariable String DotThamQuanId,
                                                                     @Valid @RequestBody List<String> chuyenThamQuans) {
        return new ResponseEntity<>(chuyenThamQuanService.deleteChuyenThuocDot(DotThamQuanId, chuyenThamQuans), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/10/2021
     * @param: id đợt
     * @param: list string id chuyến muốn thêm
     * @return: list chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "API update thêm chuyến vào đợt")
    @PostMapping("/update-chuyen-tham-quan-vao-dot/{DotThamQuanId}")
    public ResponseEntity<List<ChuyenThamQuan>> updateChuyenVaoDot(@PathVariable String DotThamQuanId,
                                                                   @Valid @RequestBody List<String> chuyenThamQuans) {
        return new ResponseEntity<>(chuyenThamQuanService.updateChuyenVaoDot(DotThamQuanId, chuyenThamQuans), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/06/2021
     * @param: id chuyến tham quan
     * @param: danh sách cộng tác viên tham gia
     * @return: chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Edit List CTV by ID chuyến tham quan")
    @PostMapping("/{id}/chinh-sua-danh-sach-cong-tac-vien")
    public ResponseEntity<ChuyenThamQuan> editListCongTacViens(@PathVariable String id,
                                                               @Valid @RequestBody List<String> danhSachCongTacVien,
                                                               Principal principal) {
        return new ResponseEntity<>(chuyenThamQuanService.editListCongTacViens(id, danhSachCongTacVien, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 07/14/2021
     * @param: search
     * @return: list chuyến tham quan
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "get all danh sách chuyến tham quan trạng thái hoàn tất")
    @GetMapping("/get-danh-sach-chuyen-trang-thai-hoan-tat")
    public ResponseEntity<List<ChuyenThamQuan>> getAllChuyenThamQuansActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(chuyenThamQuanService.getAllChuyenThamQuansActive(search), HttpStatus.OK);
    }

}
