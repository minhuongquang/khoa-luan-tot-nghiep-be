package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.ketquakhaosatsinhvien.KetQuaKhaoSatSinhVienDto;
import com.stc.thamquan.entities.KetQuaKhaoSatSinhVien;
import com.stc.thamquan.services.ketquakhaosatsinhvien.KetQuaKhaoSatSinhVienService;
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
 * User      : trucntt
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatSinhVienController
 */

@RestController
@RequestMapping("/rest/ket-qua-khao-sat-sinh-vien")
public class KetQuaKhaoSatSinhVienController {
    private final KetQuaKhaoSatSinhVienService ketQuaKhaoSatSinhVienService;

    public KetQuaKhaoSatSinhVienController(KetQuaKhaoSatSinhVienService ketQuaKhaoSatSinhVienService) {
        this.ketQuaKhaoSatSinhVienService = ketQuaKhaoSatSinhVienService;
    }

    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm kết quả khảo sát sinh viên theo chuyến tham quan, sinh viên, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách kết quả khảo sát sinh viên phân trang cho admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all kết quả khảo sát sinh viên phân trang cho admin, tìm theo chuyến tham quan, sinh viên, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<KetQuaKhaoSatSinhVien>> getAllKetQuaKhaoSatSinhViensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "chuyenThamQuan") String column) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.getAllKetQuaKhaoSatSinhViensPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách kết quả khảo sát sinh viên active
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'SINH_VIEN')")
    @ApiOperation(value = "Get all kết quả khảo sát sinh viên active")
    @GetMapping()
    public ResponseEntity<List<KetQuaKhaoSatSinhVien>> getAllKetQuaKhaoSatSinhViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.getAllKetQuaKhaoSatSinhViensActive(search), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát sinh viên cần lấy thông tin
     * @return; kết quả khảo sát sinh viên có id tương ứng trong hệ thống
     */
    @ApiOperation(value = "Get kết quả khảo sát sinh viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatSinhVien> getKetQuaKhaoSatSinhVien(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.getKetQuaKhaoSatSinhVien(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo kết quả khảo sát sinh viên
     * @return; kết quả khảo sát sinh viên mới được tạo
     */
    @PreAuthorize("hasRole('SINH_VIEN')")
    @ApiOperation(value = "Create kết quả khảo sát sinh viên")
    @PostMapping
    public ResponseEntity<List<KetQuaKhaoSatSinhVien>> createKetQuaKhaoSatSinhVien(@Valid @RequestBody KetQuaKhaoSatSinhVienDto dto,
                                                                                   Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.createKetQuaKhaoSatSinhVien(dto, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát sinh viên cần thay đổi trạng thái
     * @return: kết quả khảo sát sinh viên có id tương ứng đã được chuyển trạng thái
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status kết quả khảo sát sinh viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatSinhVien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.changeStatus(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param chuyenThamQuanId: Id chuyến tham quan
     * @return: false: chưa khảo sát, true: đã khảo sát
     */
    @PreAuthorize("hasRole('SINH_VIEN')")
    @ApiOperation(value = "Kiểm tra thực khảo sát (false: chưa khảo sát)")
    @PostMapping("/kiem-tra-thuc-hien-khao-sat/{chuyenThamQuanId}")
    public ResponseEntity<Boolean> checkKhaoSat(@PathVariable String chuyenThamQuanId,
                                                Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatSinhVienService.checkKhaoSat(chuyenThamQuanId,principal ), HttpStatus.OK);
    }

}
