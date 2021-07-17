package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.phuongtien.PhuongTienDto;
import com.stc.thamquan.entities.PhuongTien;
import com.stc.thamquan.services.phuongtien.PhuongTienService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/phuong-tien")

public class PhuongTienController {
    
    private final PhuongTienService phuongTienService;

    public PhuongTienController(PhuongTienService phuongTienService) {
        this.phuongTienService = phuongTienService;
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
    @ApiOperation(value = "Get all phương tiện paging admin")
    @GetMapping("/paging")
    public ResponseEntity<Page<PhuongTien>> getAllPhuongTiensPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenPhuongTien") String column) {
        return new ResponseEntity<>(phuongTienService.getAllPhuongTiensPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @return
     */
    @ApiOperation(value = "Get danh sách phương tiện active")
    @GetMapping
    public ResponseEntity<List<PhuongTien>> getAllPhuongTiensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(phuongTienService.getAllPhuongTiensActive(search), HttpStatus.OK);
    }


    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id phương tiện cần lấy
     * @return: phương tiện tìm thấy trong db
     */
    @ApiOperation(value = "Get phuoưng tiện by id")
    @GetMapping("/{id}")
    public ResponseEntity<PhuongTien> getPhuongTien(@PathVariable String id) {
        return new ResponseEntity<>(phuongTienService.getPhuongTien(id), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param dto: DTO tạo phương tiện
     * @return: phương tiện đã được tạo thành công
     */
    @ApiOperation(value = "Create phương tiện")
    @PostMapping
    public ResponseEntity<PhuongTien> createPhuongTien(@Valid @RequestBody PhuongTienDto dto) {
        return new ResponseEntity<>(phuongTienService.createPhuongTien(dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id phương tiện cần update
     * @param dto: DTO update phương tiện
     * @return: phương tiện đã được update
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update phương tiện")
    @PutMapping("/{id}")
    public ResponseEntity<PhuongTien> updatePhuongTien(@PathVariable String id, @Valid @RequestBody PhuongTienDto dto) {
        return new ResponseEntity<>(phuongTienService.updatePhuongTien(id, dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id phương tiện cần thay đổi trạng thái
     * @return
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status phương tiện")
    @DeleteMapping("/{id}")
    public ResponseEntity<PhuongTien> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(phuongTienService.changeStatus(id), HttpStatus.OK);
    }
}
