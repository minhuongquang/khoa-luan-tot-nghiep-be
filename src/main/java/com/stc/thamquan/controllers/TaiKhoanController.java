package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.taikhoan.TaiKhoanDto;
import com.stc.thamquan.entities.TaiKhoan;
import com.stc.thamquan.services.taikhoan.TaiKhoanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:15
 * Filename  : TaiKhoanController
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/rest/tai-khoan")
public class TaiKhoanController {
    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    /***
     * @author: thangpx
     * @createdDate: 08/04/2021
     * @return: Get danh sách role dùng cho bảng tài khoản, cho phép admin tạo admin mới hoặc tạo thư ký
     */
    @GetMapping("/roles")
    public ResponseEntity<List<String>> getRoleTaiKhoans() {
        return new ResponseEntity<>(taiKhoanService.getRoleTaiKhoans(), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm tài khoản theo email, họ tên
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách tài khoản phân trang cho admin
     */
    @ApiOperation(value = "Get all tài khoản phân trang cho admin, tìm theo email, họ tên")
    @GetMapping("/paging")
    public ResponseEntity<Page<TaiKhoan>> getAllTaiKhoansPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "hoTen") String column) {
        return new ResponseEntity<>(taiKhoanService.getAllTaiKhoanPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @param id: Id tài khoản cần lấy thông tin
     * @return; Tài khoản có id tương ứng trong hệ thống
     */
    @ApiOperation(value = "Get tài khoản by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaiKhoan> getTaiKhoan(@PathVariable String id) {
        return new ResponseEntity<>(taiKhoanService.getTaiKhoan(id), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @param dto: DTO tạo tài khoản
     * @return; Tài khoản mới được tạo nếu email chưa tồn tại
     */
    @ApiOperation(value = "Create tài khoản")
    @PostMapping
    public ResponseEntity<TaiKhoan> createTaiKhoan(@Valid @RequestBody TaiKhoanDto dto) {
        return new ResponseEntity<>(taiKhoanService.createTaiKhoan(dto), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @param id: Id tài khoản cần update thông tin
     * @param dto: DTO update thông tin tài khoản, không cho cập nhật email
     * @return: Tài khoản có id tương ứng cần update thông tin
     */
    @ApiOperation(value = "Update tài khoản")
    @PutMapping("/{id}")
    public ResponseEntity<TaiKhoan> updateTaiKhoan(@PathVariable String id, @Valid @RequestBody TaiKhoanDto dto) {
        return new ResponseEntity<>(taiKhoanService.updateTaiKhoan(id, dto), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @param id: Id tài khoản cần thay đổi trạng thái
     * @return: Tài khoản có id tương ứng đã được chuyển trạng thái
     */
    @ApiOperation(value = "change status tài khoản")
    @DeleteMapping("/{id}")
    public ResponseEntity<TaiKhoan> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(taiKhoanService.changeStatus(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get thông tin tài khoản đăng nhập hiện tại")
    @GetMapping("/current")
    public ResponseEntity<TaiKhoan> getCurrent(Principal principal) {
        return new ResponseEntity<>(taiKhoanService.getCurrent(principal), HttpStatus.OK);
    }
}
