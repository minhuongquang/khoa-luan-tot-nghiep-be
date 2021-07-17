package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.ketquakhaosatdoanhnghiep.KetQuaKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.KetQuaKhaoSatDoanhNghiep;
import com.stc.thamquan.services.ketquakhaosatdoanhnghiep.KetQuaKhaoSatDoanhNghiepService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatDoanhNghiepController
 */
@RestController
@RequestMapping("/rest/ket-qua-khao-sat-doanh-nghiep")
public class KetQuaKhaoSatDoanhNghiepController {
    private final KetQuaKhaoSatDoanhNghiepService ketQuaKhaoSatDoanhNghiepService;

    public KetQuaKhaoSatDoanhNghiepController(KetQuaKhaoSatDoanhNghiepService ketQuaKhaoSatDoanhNghiepService) {
        this.ketQuaKhaoSatDoanhNghiepService = ketQuaKhaoSatDoanhNghiepService;
    }

    /***
     * @author: truc
     * @createdDate: 08/04/2021
     * @param search : Từ khóa tìm kiếm kết quả khảo sát doanh nghiệp theo chuyến tham quan, doanh nghiệp, lựa chọn tối đa
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách kết quả khảo sát doanh nghiệp phân trang cho admin
     */
    //   @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all kết quả khảo sát doanh nghiệp phân trang cho admin, tìm theo chuyến tham quan, doanh nghiệp, lựa chọn tối đa")
    @GetMapping("/paging")
    public ResponseEntity<Page<KetQuaKhaoSatDoanhNghiep>> getAllKetQuaKhaoSatDoanhNghiepsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "chuyenThamQuan") String column) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.getAllKetQuaKhaoSatDoanhNghiepsPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @return; danh sách kết quả khảo sát doanh nghiệp active
     */
    //   @PreAuthorize("hasAnyRole('DOANH_NGHIEP', 'ADMIN')")
    @ApiOperation(value = "Get all kết quả khảo sát doanh nghiệp active")
    @GetMapping()
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiep>> getAllKetQuaKhaoSatDoanhNghiepsActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.getAllKetQuaKhaoSatDoanhNghiepsActive(search), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát doanh nghiệp cần lấy thông tin
     * @return; kết quả khảo sát doanh nghiệp có id tương ứng trong hệ thống
     */
    @ApiOperation(value = "Get kết quả khảo sát doanh nghiệp by id")
    @GetMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatDoanhNghiep> getKetQuaKhaoSatDoanhNghiep(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.getKetQuaKhaoSatDoanhNghiep(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param dto: DTO tạo kết quả khảo sát doanh nghiệp
     * @return; kết quả khảo sát doanh nghiệp mới được tạo nếu email chưa tồn tại
     */
    //  @PreAuthorize("hasRole('DOANH_NGHIEP')")
    @ApiOperation(value = "Create kết quả khảo sát doanh nghiệp")
    @PostMapping
    public ResponseEntity<List<KetQuaKhaoSatDoanhNghiep>> createKetQuaKhaoSatDoanhNghiep(
            @Valid @RequestBody KetQuaKhaoSatDoanhNghiepDto dto,
            Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.createKetQuaKhaoSatDoanhNghiep(dto, principal), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @param id: Id kết quả khảo sát doanh nghiệp cần thay đổi trạng thái
     * @return: kết quả khảo sát doanh nghiệp có id tương ứng đã được chuyển trạng thái
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status kết quả khảo sát doanh nghiệp")
    @DeleteMapping("/{id}")
    public ResponseEntity<KetQuaKhaoSatDoanhNghiep> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.changeStatus(id), HttpStatus.OK);
    }
    /***
     * @author: truc
     * @param chuyenThamQuanId: Id chuyến tham quan
     * @return: false: chưa khảo sát, true: đã khảo sát
     */
    @PreAuthorize("hasRole('DOANH_NGHIEP')")
    @ApiOperation(value = "Kiểm tra thực khảo sát (false: chưa khảo sát)")
    @PostMapping("/kiem-tra-thuc-hien-khao-sat/{chuyenThamQuanId}")
    public ResponseEntity<Boolean> checkKhaoSat(@PathVariable String chuyenThamQuanId,
                                                Principal principal) {
        return new ResponseEntity<>(ketQuaKhaoSatDoanhNghiepService.checkKhaoSat(chuyenThamQuanId,principal ), HttpStatus.OK);
    }
}
