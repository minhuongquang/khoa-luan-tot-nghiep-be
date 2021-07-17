package com.stc.thamquan.controllers.danhmuc;


import com.stc.thamquan.dtos.doanhnghiep.DoanhNghiepDto;
import com.stc.thamquan.entities.DoanhNghiep;
import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.services.doanhnghiep.DoanhNghiepService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 4/15/21
 * Time      : 10:15
 * Filename  : DoanhNghiepController
 */

@RestController
@RequestMapping("/rest/doanh-nghiep")
public class DoanhNghiepController {
    private final DoanhNghiepService doanhNghiepService;

    public DoanhNghiepController(DoanhNghiepService doanhNghiepService) {
        this.doanhNghiepService = doanhNghiepService;
    }


    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return
     */
    @ApiOperation(value = "Get all doanh nghiệp paging admin")
    @GetMapping("/paging")
    public ResponseEntity<Page<DoanhNghiep>> getAllDoanhNghiepsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenDoanhNghiep") String column) {
        return new ResponseEntity<>(doanhNghiepService.getAllDoanhNghiepsPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @return
     */
    @ApiOperation(value = "Get danh sách doanh nghiệp active")
    @GetMapping
    public ResponseEntity<List<DoanhNghiep>> getAllDoanhNghiepsActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(doanhNghiepService.getAllDoanhNghiepsActive(search), HttpStatus.OK);
    }


    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @param id: Id doanh nghiệp cần lấy
     * @return: doanh nghiệp tìm thấy trong db
     */
    @ApiOperation(value = "Get doanh nghiệp by id")
    @GetMapping("/{id}")
    public ResponseEntity<DoanhNghiep> getDoanhNghiep(@PathVariable String id) {
        return new ResponseEntity<>(doanhNghiepService.getDoanhNghiep(id), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @param dto: DTO tạo doanh nghiệp
     * @return: doanh nghiệp đã được tạo thành công
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'GIANG_VIEN')")
    @ApiOperation(value = "Create doanh nghiệp")
    @PostMapping
    public ResponseEntity<DoanhNghiep> createDoanhNghiep(@Valid @RequestBody DoanhNghiepDto dto) {
        return new ResponseEntity<>(doanhNghiepService.createDoanhNghiep(dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @param id: Id doanh nghiệp cần update
     * @param dto: DTO update doanh nghiệp
     * @return: doanh nghiệp đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update doanh nghiệp")
    @PutMapping("/{id}")
    public ResponseEntity<DoanhNghiep> updateDoanhNghiep(@PathVariable String id, @Valid @RequestBody DoanhNghiepDto dto) {
        return new ResponseEntity<>(doanhNghiepService.updateDoanhNghiep(id, dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 15-04-2021
     * @param id: Id doanh nghiệp cần thay đổi trạng thái
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Change status doanh nghiệp")
    @DeleteMapping("/{id}")
    public ResponseEntity<DoanhNghiep> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(doanhNghiepService.changeStatus(id), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 16-04-2021
     * @param
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "import danh sách doanh nghiệp từ file excel (admin)  ")
    @PostMapping("/import-danh-sach-doanh-nghiep")
    public ResponseEntity<String> importDanhSachDoanhNghieps(@RequestParam(name="file")MultipartFile file) throws Exception {
        doanhNghiepService.importDanhSachDoanhNghieps(file);
        return new ResponseEntity<>("Import danh sách doanh nghiệp thành công", HttpStatus.OK);
    }

    @ApiOperation(value = "Get doanh nghiệp đang đăng nhập by token")
    @GetMapping("/current")
    public ResponseEntity<DoanhNghiep> getDoanhNghiep(Principal principal) {
        return new ResponseEntity<>(doanhNghiepService.getCurrentDoanhNghiep(principal), HttpStatus.OK);
    }

}
