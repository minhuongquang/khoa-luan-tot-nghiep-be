package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.cauhoikhaosatcongtacvien.CauHoiKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatCongTacVien;
import com.stc.thamquan.services.cauhoikhaosatcongtacvien.CauHoiKhaoSatCongTacVienService;
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
 * Filename  : CauHoiKhaoSatCongTacVienController
 */

@RestController
@RequestMapping("/rest/cau-hoi-khao-sat-cong-tac-vien")
public class CauHoiKhaoSatCongTacVienController {
    
    private final CauHoiKhaoSatCongTacVienService cauHoiKhaoSatCongTacVienService;

    public CauHoiKhaoSatCongTacVienController(CauHoiKhaoSatCongTacVienService cauHoiKhaoSatCongTacVienService) {
        this.cauHoiKhaoSatCongTacVienService = cauHoiKhaoSatCongTacVienService;
    }
    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm câu hỏi khảo sát cộng tác viên theo câu hỏi, loại câu hỏi, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách câu hỏi khảo sát cộng tác viên phân trang cho admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all câu hỏi khảo sát cộng tác viên phân trang cho admin, tìm theo câu hỏi, loại câu hỏi, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<CauHoiKhaoSatCongTacVien>> getAllCauHoiKhaoSatCongTacViensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "cauHoi") String column) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.getAllCauHoiKhaoSatCongTacViensPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách câu hỏi khảo sát cộng tác viên active
     */
    @PreAuthorize("hasAnyRole('CONG_TAC_VIEN','ADMIN')")
    @ApiOperation(value = "Get all câu hỏi khảo sát cộng tác viên active")
    @GetMapping("/khao-sat/{idChuyenThamQuan}")
    public ResponseEntity<List<CauHoiKhaoSatCongTacVien>> getAllCauHoiKhaoSatCongTacViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                                                @PathVariable String idChuyenThamQuan) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.getAllCauHoiKhaoSatCongTacViensActive(search, idChuyenThamQuan), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát cộng tác viên cần lấy thông tin
     * @return; câu hỏi khảo sát cộng tác viên có id tương ứng trong hệ thống
     */
    @PreAuthorize("hasAnyRole('CONG_TAC_VIEN','ADMIN')")
    @ApiOperation(value = "Get câu hỏi khảo sát cộng tác viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatCongTacVien> getCauHoiKhaoSatCongTacVien(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.getCauHoiKhaoSatCongTacVien(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo câu hỏi khảo sát cộng tác viên
     * @return; câu hỏi khảo sát cộng tác viên mới được tạo nếu email chưa tồn tại
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create câu hỏi khảo sát cộng tác viên")
    @PostMapping
    public ResponseEntity<CauHoiKhaoSatCongTacVien> createCauHoiKhaoSatCongTacVien(@Valid @RequestBody CauHoiKhaoSatCongTacVienDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.createCauHoiKhaoSatCongTacVien(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát cộng tác viên cần update thông tin
     * @param dto: DTO update thông tin câu hỏi khảo sát cộng tác viên
     * @return: câu hỏi khảo sát cộng tác viên có id tương ứng cần update thông tin
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update câu hỏi khảo sát cộng tác viên")
    @PutMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatCongTacVien> updateCauHoiKhaoSatCongTacVien(@PathVariable String id, @Valid @RequestBody CauHoiKhaoSatCongTacVienDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.updateCauHoiKhaoSatCongTacVien(id, dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát cộng tác viên cần thay đổi trạng thái
     * @return: câu hỏi khảo sát cộng tác viên có id tương ứng đã được chuyển trạng thái
     */
     @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status câu hỏi khảo sát cộng tác viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatCongTacVien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatCongTacVienService.changeStatus(id), HttpStatus.OK);
    }
}
