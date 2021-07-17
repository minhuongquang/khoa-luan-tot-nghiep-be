package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.cauhoikhaosatdoanhnghiep.CauHoiKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import com.stc.thamquan.services.cauhoikhaosatdoanhnghiep.CauHoiKhaoSatDoanhNghiepService;
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
 * Filename  : CauHoiKhaoSatDoanhNghiepController
 */

@RestController
@RequestMapping("/rest/cau-hoi-khao-sat-doanh-nghiep")
public class CauHoiKhaoSatDoanhNghiepController {
    private final CauHoiKhaoSatDoanhNghiepService cauHoiKhaoSatDoanhNghiepService;

    public CauHoiKhaoSatDoanhNghiepController(CauHoiKhaoSatDoanhNghiepService cauHoiKhaoSatDoanhNghiepService) {
        this.cauHoiKhaoSatDoanhNghiepService = cauHoiKhaoSatDoanhNghiepService;
    }


    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm câu hỏi khảo sát doanh nghiệp theo câu hỏi, loại câu hỏi, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách câu hỏi khảo sát doanh nghiệp phân trang cho admin
     */
     @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all câu hỏi khảo sát doanh nghiệp phân trang cho admin, tìm theo câu hỏi, loại câu hỏi, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<CauHoiKhaoSatDoanhNghiep>> getAllCauHoiKhaoSatDoanhNghiepsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "cauHoi") String column) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.getAllCauHoiKhaoSatDoanhNghiepsPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách câu hỏi khảo sát doanh nghiệp active
     */
     @PreAuthorize("hasAnyRole('DOANH_NGHIEP','ADMIN')")
    @ApiOperation(value = "Get all câu hỏi khảo sát doanh nghiệp active")
    @GetMapping("/khao-sat/{idChuyenThamQuan}")
    public ResponseEntity<List<CauHoiKhaoSatDoanhNghiep>> getAllCauHoiKhaoSatDoanhNghiepsActive(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                                                @PathVariable String idChuyenThamQuan) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.getAllCauHoiKhaoSatDoanhNghiepsActive(search, idChuyenThamQuan), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát doanh nghiệp cần lấy thông tin
     * @return; câu hỏi khảo sát doanh nghiệp có id tương ứng trong hệ thống
     */
    @PreAuthorize("hasAnyRole('DOANH_NGHIEP','ADMIN')")
    @ApiOperation(value = "Get câu hỏi khảo sát doanh nghiệp by id")
    @GetMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatDoanhNghiep> getCauHoiKhaoSatDoanhNghiep(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.getCauHoiKhaoSatDoanhNghiep(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo câu hỏi khảo sát doanh nghiệp
     * @return; câu hỏi khảo sát doanh nghiệp mới được tạo nếu email chưa tồn tại
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create câu hỏi khảo sát doanh nghiệp")
    @PostMapping
    public ResponseEntity<CauHoiKhaoSatDoanhNghiep> createCauHoiKhaoSatDoanhNghiep(@Valid @RequestBody CauHoiKhaoSatDoanhNghiepDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.createCauHoiKhaoSatDoanhNghiep(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát doanh nghiệp cần update thông tin
     * @param dto: DTO update thông tin câu hỏi khảo sát doanh nghiệp
     * @return: câu hỏi khảo sát doanh nghiệp có id tương ứng cần update thông tin
     */
     @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update câu hỏi khảo sát doanh nghiệp")
    @PutMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatDoanhNghiep> updateCauHoiKhaoSatDoanhNghiep(@PathVariable String id, @Valid @RequestBody CauHoiKhaoSatDoanhNghiepDto dto) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.updateCauHoiKhaoSatDoanhNghiep(id, dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id câu hỏi khảo sát doanh nghiệp cần thay đổi trạng thái
     * @return: câu hỏi khảo sát doanh nghiệp có id tương ứng đã được chuyển trạng thái
     */
     @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status câu hỏi khảo sát doanh nghiệp")
    @DeleteMapping("/{id}")
    public ResponseEntity<CauHoiKhaoSatDoanhNghiep> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(cauHoiKhaoSatDoanhNghiepService.changeStatus(id), HttpStatus.OK);
    }
}
