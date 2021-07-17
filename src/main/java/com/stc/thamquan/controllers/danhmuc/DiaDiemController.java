package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.diadiem.DiaDiemDto;
import com.stc.thamquan.entities.DiaDiem;
import com.stc.thamquan.services.diadiem.DiaDiemService;
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
 * Date      : 4/12/21
 * Time      : 18:06
 * Filename  : DiaDiemController
 */
@RestController
@RequestMapping("/rest/dia-diem")
public class DiaDiemController {
    private final DiaDiemService diaDiemService;

    public DiaDiemController(DiaDiemService diaDiemService) {
        this.diaDiemService = diaDiemService;
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return
     */
    @ApiOperation(value = "Get all địa điểm paging admin")
    @GetMapping("/paging")
    public ResponseEntity<Page<DiaDiem>> getAllDiaDiemsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenDiaDiem") String column) {
        return new ResponseEntity<>(diaDiemService.getAllDiaDiemsPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @return
     */
    @ApiOperation(value = "Get danh sách địa điểm active")
    @GetMapping
    public ResponseEntity<List<DiaDiem>> getAllDiaDiemsActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(diaDiemService.getAllDiaDiemsActive(search), HttpStatus.OK);
    }


    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id địa điểm cần lấy
     * @return: Địa điểm tìm thấy trong db
     */
    @ApiOperation(value = "Get địa điểm by id")
    @GetMapping("/{id}")
    public ResponseEntity<DiaDiem> getDiaDiem(@PathVariable String id) {
        return new ResponseEntity<>(diaDiemService.getDiaDiem(id), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param dto: DTO tạo địa điểm
     * @return: Địa điểm đã được tạo thành công
     */
    @ApiOperation(value = "Create địa điểm")
    @PostMapping
    public ResponseEntity<DiaDiem> createDiaDiem(@Valid @RequestBody DiaDiemDto dto) {
        return new ResponseEntity<>(diaDiemService.createDiaDiem(dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id địa điểm cần update
     * @param dto: DTO update địa điểm
     * @return: Địa điểm đã được update
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update địa điểm")
    @PutMapping("/{id}")
    public ResponseEntity<DiaDiem> updateDiaDiem(@PathVariable String id, @Valid @RequestBody DiaDiemDto dto) {
        return new ResponseEntity<>(diaDiemService.updateDiaDiem(id, dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id địa điểm cần thay đổi trạng thái
     * @return
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status địa điểm")
    @DeleteMapping("/{id}")
    public ResponseEntity<DiaDiem> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(diaDiemService.changeStatus(id), HttpStatus.OK);
    }
}
