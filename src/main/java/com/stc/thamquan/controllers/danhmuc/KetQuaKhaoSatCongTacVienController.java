package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.ketquakhaosatcongtacvien.KetQuaKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.KetQuaKhaoSatCongTacVien;
import com.stc.thamquan.services.ketquakhaosatcongtacvien.KetQuaKhaoSatCongTacVienService;
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
 * Filename  : KetQuaKhaoSatCongTacVienController
 */
@RestController
@RequestMapping("/rest/ket-qua-khao-sat-cong-tac-vien")
public class KetQuaKhaoSatCongTacVienController {
    private final KetQuaKhaoSatCongTacVienService ketQuaKhaoSatCongTacVienService;

    public KetQuaKhaoSatCongTacVienController(KetQuaKhaoSatCongTacVienService ketQuaKhaoSatCongTacVienService) {
        this.ketQuaKhaoSatCongTacVienService = ketQuaKhaoSatCongTacVienService;
    }

    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm kết quả khảo sát cộng tác viên theo chuyến tham quan, cộng tác viên, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách kết quả khảo sát cộng tác viên phân trang cho admin
     */
    //   @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all kết quả khảo sát cộng tác viên phân trang cho admin, tìm theo chuyến tham quan, cộng tác viên, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<KetQuaKhaoSatCongTacVien>> getAllKetQuaKhaoSatCongTacViensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "chuyenThamQuan") String column) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.getAllKetQuaKhaoSatCongTacViensPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách kết quả khảo sát cộng tác viên active
     */
    //   @PreAuthorize("hasAnyRole('CONG_TAC_VIEN', 'ADMIN')")
    @ApiOperation(value = "Get all kết quả khảo sát cộng tác viên active")
    @GetMapping()
    public ResponseEntity<List<KetQuaKhaoSatCongTacVien>> getAllKetQuaKhaoSatCongTacViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.getAllKetQuaKhaoSatCongTacViensActive(search), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát cộng tác viên cần lấy thông tin
     * @return; kết quả khảo sát cộng tác viên có id tương ứng trong hệ thống
     */
    @ApiOperation(value = "Get kết quả khảo sát cộng tác viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatCongTacVien> getKetQuaKhaoSatCongTacVien(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.getKetQuaKhaoSatCongTacVien(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo kết quả khảo sát cộng tác viên
     * @return; kết quả khảo sát cộng tác viên mới được tạo nếu email chưa tồn tại
     */
    //  @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @ApiOperation(value = "Create kết quả khảo sát cộng tác viên")
    @PostMapping("")
    public ResponseEntity<List<KetQuaKhaoSatCongTacVien>> createKetQuaKhaoSatCongTacVien(@Valid @RequestBody KetQuaKhaoSatCongTacVienDto dto,
                                                                                         Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.createKetQuaKhaoSatCongTacVien(dto, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát cộng tác viên cần thay đổi trạng thái
     * @return: kết quả khảo sát cộng tác viên có id tương ứng đã được chuyển trạng thái
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status kết quả khảo sát cộng tác viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatCongTacVien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.changeStatus(id), HttpStatus.OK);
    }
    /***
     * @author: truc
     * @param chuyenThamQuanId: Id chuyến tham quan
     * @return: false: chưa khảo sát, true: đã khảo sát
     */
    @PreAuthorize("hasRole('CONG_TAC_VIEN')")
    @ApiOperation(value = "Kiểm tra thực khảo sát (false: chưa khảo sát)")
    @PostMapping("/kiem-tra-thuc-hien-khao-sat/{chuyenThamQuanId}")
    public ResponseEntity<Boolean> checkKhaoSat(@PathVariable String chuyenThamQuanId,
                                                Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatCongTacVienService.checkKhaoSat(chuyenThamQuanId,principal ), HttpStatus.OK);
    }

}
