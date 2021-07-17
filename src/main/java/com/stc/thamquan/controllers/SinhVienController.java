package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.giangvien.FilterGiangVienDto;
import com.stc.thamquan.dtos.giangvien.GiangVienDto;
import com.stc.thamquan.dtos.sinhvien.FilterSinhVienDto;
import com.stc.thamquan.dtos.sinhvien.SinhVienDto;
import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.services.sinhvien.SinhVienService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/sinh-vien")
public class SinhVienController {
    private final SinhVienService sinhVienService;

    public SinhVienController(SinhVienService sinhVienService) {
        this.sinhVienService = sinhVienService;
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param dto: DTO filter thông tin sinh viên
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách sinh viên đã được phân trang
     */
    @ApiOperation(value = "Filter sinh viên theo mã, email, họ tên, khoa, ngành")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<Page<SinhVien>> filter(@Valid @RequestBody FilterSinhVienDto dto,
                                                 @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
                                                 @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
                                                 @RequestParam(name = "column", required = false, defaultValue = "maSV") String column) {

        return new ResponseEntity<>(sinhVienService.filter(dto, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param id: Id sinh viên cần lấy thông tin
     * @return: Sinh viên có id tương ứng trong cơ sở dữ liệu
     */
    @ApiOperation(value = "Get sinh viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<SinhVien> getSinhVien(@PathVariable String id) {
        return new ResponseEntity<>(sinhVienService.getSinhVien(id), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param principal: token của sinh viên đăng login vào hệ thống
     * @return: Sinh viên đang đăng nhập vào hệ thống
     */
    @PreAuthorize("hasRole('SINH_VIEN')")
    @ApiOperation(value = "Get sinh viên đang đăng nhập by token")
    @GetMapping("/current")
    public ResponseEntity<SinhVien> getSinhVien(Principal principal) {
        return new ResponseEntity<>(sinhVienService.getCurrentSinhVien(principal), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param dto : DTO tạo mới sinh viên
     * @return: Sinh viên được tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create sinh viên")
    @PostMapping
    public ResponseEntity<SinhVien> createSinhVien(@Valid @RequestBody SinhVienDto dto) {
        return new ResponseEntity<>(sinhVienService.createSinhVien(dto), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param id: Id sinh viên cần cập nhật thông tin
     * @param dto: DTO cập nhật thông tin sinh viên (ko cho cập nhật email)
     * @return: Sinh viên được update thông tin thành công
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'SINH_VIEN')")
    @ApiOperation(value = "Update sinh viên")
    @PutMapping("/{id}")
    public ResponseEntity<SinhVien> updateSinhVien(@PathVariable String id, @Valid @RequestBody SinhVienDto dto) {
        return new ResponseEntity<>(sinhVienService.updateSinhVien(id, dto), HttpStatus.OK);
    }

    /***
     * @author: nduong
     * @createdDate: 14-05-2021
     * @param id: Id sinh viên cần thay đổi trạng thái
     * @return: Sinh viên có id tương ứng đã được thay đổi trạng thái
     */
    @ApiOperation(value = "Thay đổi trạng thái sinh viên")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<SinhVien> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(sinhVienService.changeStatus(id), HttpStatus.OK);
    }
    /***
     * @author: truc
     * @createdDate: 07-06-2021
     * @param search: tìm kiếm theo mã, họ tên, lớp
     * @return: danh sách sinh viên active
     */
    @ApiOperation(value = "Get all sinh viên active, tìm kiếm theo mã, họ tên, lớp")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<SinhVien>> getAllSinhViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(sinhVienService.getAllSinhViensActive(search), HttpStatus.OK);
    }

}
