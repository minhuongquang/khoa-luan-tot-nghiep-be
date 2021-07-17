package com.stc.thamquan.controllers.danhmuc;


import com.stc.thamquan.dtos.cauhoikhaosatsinhvien.CauHoiKhaoSatSinhVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatSinhVien;
import com.stc.thamquan.services.cauhoikhaosatsinhvien.CauHoiKhaoSatSinhVienService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : CauHoiKhaoSatSinhVienController
 */

@RestController
@RequestMapping("/rest/cau-hoi-khao-sat-sinh-vien")
public class CauHoiKhaoSatSinhVienController {
    private final CauHoiKhaoSatSinhVienService cauHoiKhaoSatSinhVienService;

    public CauHoiKhaoSatSinhVienController(CauHoiKhaoSatSinhVienService cauHoiKhaoSatSinhVienService) {
        this.cauHoiKhaoSatSinhVienService = cauHoiKhaoSatSinhVienService;
    }


    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm câu hỏi khảo sát sinh viên theo câu hỏi, loại câu hỏi, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách câu hỏi khảo sát sinh viên phân trang cho admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all câu hỏi khảo sát sinh viên phân trang cho admin, tìm theo câu hỏi, loại câu hỏi, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<CauHoiKhaoSatSinhVien>> getAllCauHoiKhaoSatSinhViensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "cauHoi") String column) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.getAllCauHoiKhaoSatSinhViensPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách câu hỏi khảo sát  sinh viên active
     */
    @PreAuthorize("hasAnyRole('SINH_VIEN','ADMIN')")

    @ApiOperation(value = "Get all câu hỏi khảo sát  sinh viên active")
    @GetMapping("/khao-sat/{idChuyenThamQuan}")
    public ResponseEntity<List<CauHoiKhaoSatSinhVien>> getAllCauHoiKhaoSatSinhViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                                          @PathVariable String idChuyenThamQuan) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.getAllCauHoiKhaoSatSinhViensActive(search, idChuyenThamQuan), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát  sinh viên cần lấy thông tin
     * @return; câu hỏi khảo sát  sinh viên  có id tương ứng trong hệ thống
     */
    @PreAuthorize("hasAnyRole('SINH_VIEN','ADMIN')")
    @ApiOperation(value = "Get câu hỏi khảo sát sinh viên  by id")
    @GetMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatSinhVien> getCauHoiKhaoSatSinhVien(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.getCauHoiKhaoSatSinhVien(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo câu hỏi khảo sát sinh viên
     * @return; câu hỏi khảo sát sinh viên  mới được tạo nếu email chưa tồn tại
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create câu hỏi khảo sát sinh viên")
    @PostMapping
    public ResponseEntity<CauHoiKhaoSatSinhVien> createCauHoiKhaoSatSinhVien(@Valid @RequestBody CauHoiKhaoSatSinhVienDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.createCauHoiKhaoSatSinhVien(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát sinh viên cần update thông tin
     * @param dto: DTO update thông tin câu hỏi khảo sát sinh viên
     * @return: câu hỏi khảo sát sinh viên  có id tương ứng cần update thông tin
     */
    @PreAuthorize("hasRole('ADMIN')")

    @ApiOperation(value = "Update câu hỏi khảo sát sinh viên")
    @PutMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatSinhVien> updateCauHoiKhaoSatSinhVien(@PathVariable String id, @Valid @RequestBody CauHoiKhaoSatSinhVienDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.updateCauHoiKhaoSatSinhVien(id, dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát  sinh viên  cần thay đổi trạng thái
     * @return: câu hỏi khảo sát  sinh viên  có id tương ứng đã được chuyển trạng thái
     */
    @PreAuthorize("hasRole('ADMIN')")

    @ApiOperation(value = "change status câu hỏi khảo sát sinh viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatSinhVien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatSinhVienService.changeStatus(id), HttpStatus.OK);
    }
}
