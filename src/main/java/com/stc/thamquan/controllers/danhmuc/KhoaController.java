package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.khoa.KhoaDto;
import com.stc.thamquan.entities.Khoa;
import com.stc.thamquan.services.khoa.KhoaService;
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
 * Time      : 14:53
 * Filename  : KhoaController
 */
@RestController
@RequestMapping("/rest/khoa")
public class KhoaController {
    private final KhoaService khoaService;

    public KhoaController(KhoaService khoaService) {
        this.khoaService = khoaService;
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các khoa được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Khoa Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<Khoa>> getKhoaPaging(
            @RequestParam(value = "search", required = false, defaultValue = "")String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tenKhoa")String column){
        return  new ResponseEntity<>(khoaService.getKhoaPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param search
     * @return: Danh sách các khoa active
     */
    @ApiOperation(value = "Get list khoa active (trangThai: true)")
    @GetMapping
    public ResponseEntity<List<Khoa>>getAllKhoaActive(
            @RequestParam(value = "search", required = false, defaultValue = "")String search){
        return new ResponseEntity<>(khoaService.getAllKhoas(search), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id khoa cần tim
     * @return: Khoa muốn tìm theo id
     */
    @ApiOperation(value = "Get Khoa by id")
    @GetMapping("/{id}")
    public ResponseEntity<Khoa>getKhoa(@PathVariable String id){
        return new ResponseEntity<>(khoaService.getKhoa(id), HttpStatus.OK);
    }


    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param khoaDto: DTO dùng để tạo khoa
     * @return: Khoa đã tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create Khoa")
    @PostMapping
    public ResponseEntity<Khoa>createKhoa(@Valid @RequestBody KhoaDto khoaDto){
        return new ResponseEntity<>(khoaService.createKhoa(khoaDto), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id khoa cần update
     * @param khoaDto: DTO dùng để update khoa
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update Khoa")
    @PutMapping("/{id}")
    public ResponseEntity<Khoa>updateKhoa(@PathVariable String id ,@Valid @RequestBody KhoaDto khoaDto){
        return new ResponseEntity<>(khoaService.updateKhoa(id, khoaDto), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id khoa cần update
     * @return:
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete Khoa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Khoa>deleteKhoa(@PathVariable String id){
        return new ResponseEntity<>(khoaService.changeStatus(id),HttpStatus.OK);
    }
}
