package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.nganh.NganhDto;
import com.stc.thamquan.entities.Nganh;
import com.stc.thamquan.services.nganh.NganhService;
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
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:54
 * Filename  : NganhController
 */
@RestController
@RequestMapping("/rest/nganh")
public class NganhController {
    private final NganhService nganhService;

    public NganhController(NganhService nganhService) {
        this.nganhService = nganhService;
    }


    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @param search: Từ khóa tìm kiếm ngành tìm theo mã, tên
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all ngành phân trang tìm kiếm theo mã, tên")
    @GetMapping("/paging")
    public ResponseEntity<Page<Nganh>> getAllNganhsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "maNganh") String column) {
        return new ResponseEntity<>(nganhService.getNganhPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @param search: Từ khóa tìm kiếm ngành tìm theo mã, tên
     * @return
     */
    @ApiOperation(value = "Get all ngành active")
    @GetMapping
    public ResponseEntity<List<Nganh>> getAllNganhActive(
            @RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(nganhService.getAllNganhs(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all ngành by khoa id")
    @GetMapping("/khoa/{khoaId}")
    public ResponseEntity<List<Nganh>> getAllNganhByKhoaId(@PathVariable String khoaId) {
        return new ResponseEntity<>(nganhService.getAllNganhByKhoaId(khoaId), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @param id: Id học ngành lấy
     * @return: Ngành được tìm thấy trong db
     */
    @ApiOperation(value = "Get ngành by id")
    @GetMapping("/{id}")
    public ResponseEntity<Nganh> getNganh(@PathVariable String id) {
        return new ResponseEntity<>(nganhService.getNganh(id), HttpStatus.OK);
    }


    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @param dto: DTO create ngành
     * @return: Ngành đã được create
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "create ngành")
    @PostMapping
    public ResponseEntity<Nganh> createNganh(@Valid @RequestBody NganhDto dto) {
        return new ResponseEntity<>(nganhService.createNganh(dto), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @param id: Id ngành cần update
     * @param dto: DTO update ngành
     * @return: Ngành đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update ngành")
    @PutMapping("/{id}")
    public ResponseEntity<Nganh> updateNganh(@PathVariable String id, @Valid @RequestBody NganhDto dto) {
        return new ResponseEntity<>(nganhService.updateNganh(id, dto), HttpStatus.OK);
    }

    /***
     * @author: hung
     * @createDate: 06-04-2021
     * @param id: Id ngành cần thay đổi trạng thái
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status ngành")
    @DeleteMapping("/{id}")
    public ResponseEntity<Nganh> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(nganhService.changeStatus(id), HttpStatus.OK);
    }
}
