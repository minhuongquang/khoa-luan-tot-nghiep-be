package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.congtacvien.CongTacVienDto;
import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.services.congtacvien.CongTacVienService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 15:15
 * Filename  : CongTacVienController
 */
@RestController
@RequestMapping("/rest/cong-tac-vien")

public class CongTacVienController {
    private final CongTacVienService congTacVienService;

    public CongTacVienController(CongTacVienService congTacVienService) {
        this.congTacVienService = congTacVienService;
    }

    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm cộng tác viên theo email, họ tên
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách cộng tác viên phân trang cho admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all cộng tác viên phân trang cho admin, tìm theo email, họ tên")
    @GetMapping("/paging")
    public ResponseEntity<Page<CongTacVien>> getAllCongTacViensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "hoTen") String column) {
        return new ResponseEntity<>(congTacVienService.getAllCongTacVienPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id cộng tác viên cần lấy thông tin
     * @return; cộng tác viên có id tương ứng trong hệ thống
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get cộng tác viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<CongTacVien> getCongTacVien(@PathVariable String id) {
        return new ResponseEntity<>(congTacVienService.getCongTacVien(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo cộng tác viên
     * @return; cộng tác viên mới được tạo nếu email chưa tồn tại
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create cộng tác viên")
    @PostMapping
    public ResponseEntity<CongTacVien> createCongTacVien(@Valid @RequestBody CongTacVienDto dto) {
        return new ResponseEntity<>(congTacVienService.createCongTacVien(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id cộng tác viên cần update thông tin
     * @param dto: DTO update thông tin cộng tác viên, không cho cập nhật email
     * @return: cộng tác viên có id tương ứng cần update thông tin
     */
    @PreAuthorize("hasAnyRole('ADMIN','CONG_TAC_VIEN')")
    @ApiOperation(value = "Update cộng tác viên")
    @PutMapping("/{id}")
    public ResponseEntity<CongTacVien> updateCongTacVien(@PathVariable String id, @Valid @RequestBody CongTacVienDto dto) {
        return new ResponseEntity<>(congTacVienService.updateCongTacVien(id, dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id cộng tác viên cần thay đổi trạng thái
     * @return: cộng tác viên có id tương ứng đã được chuyển trạng thái
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status cộng tác viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<CongTacVien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(congTacVienService.changeStatus(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @ApiOperation(value = "Get thông tin cộng tác viên đăng nhập hiện tại")
    @GetMapping("/current")
    public ResponseEntity<CongTacVien> getCurrent(Principal principal) {
        return new ResponseEntity<>(congTacVienService.getCurrent(principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createdDate: 06/06/2021
     * @param search: tìm kiếm theo mã, họ tên cộng tác viên
     * @return: danh sách cộng tác viên
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all cộng tác viên active")
    @GetMapping()
    public ResponseEntity<List<CongTacVien>> getAllCongTacViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(congTacVienService.getAllCongTacViensActive(search), HttpStatus.OK);
    }
}
