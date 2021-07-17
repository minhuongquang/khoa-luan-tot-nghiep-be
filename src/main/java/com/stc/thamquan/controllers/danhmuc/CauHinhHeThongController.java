package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.cauhinhhethong.CauHinhHeThongDto;
import com.stc.thamquan.entities.CauHinhHeThong;
import com.stc.thamquan.services.cauhinhhethong.CauHinhHeThongService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:14
 * Filename  : CauHinhHeThongController
 */
@RestController
@RequestMapping("/rest/cau-hinh-he-thong")
public class CauHinhHeThongController {
    private final CauHinhHeThongService cauHinhHeThongService;

    public CauHinhHeThongController(CauHinhHeThongService cauHinhHeThongService) {
        this.cauHinhHeThongService = cauHinhHeThongService;
    }

    /***
     * @author: vlong
     * @createDate: 08-04-2021
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các cấu hình hệ thống được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Cấu Hình Hệ Thống Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<CauHinhHeThong>> getAllHocViPaging(
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "emailGuiThu")String column){
        return  new ResponseEntity<>(cauHinhHeThongService.getAllCauHinhHeThongPaging( page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 08-04-2021
     * @param id: Id cấu hình hệ thống cần lấy
     * @return: Cấu hình hệ thống tìm thấy trong db
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get Cấu Hình Hệ Thống by id")
    @GetMapping("/{id}")
    public ResponseEntity<CauHinhHeThong> getCauHinhHeThong(@PathVariable String id){
        return new ResponseEntity<>(cauHinhHeThongService.getCauHinhHeThong(id),HttpStatus.OK);
    }
    /***
     * @author: vlong
     * @createDate: 08-04-2021
     * @param dto: DTO tạo
     * @return: Cấu hình hệ thống đã được tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create Cấu Hình Hệ Thống")
    @PostMapping
    public ResponseEntity<CauHinhHeThong> createCauHinhHeThong(@Valid @RequestBody CauHinhHeThongDto dto){
        return new ResponseEntity<>(cauHinhHeThongService.createCauHinhHeThong(dto),HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 08-04-2021
     * @param id: Id cấu hình hệ thống cần update
     * @param dto: DTO update cấu hình hệ thống
     * @return: Cấu hình hệ thống đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update Cấu hình hệ thống")
    @PutMapping("/{id}")
    public ResponseEntity<CauHinhHeThong> updateCauHinhHeThong(@PathVariable String id, @Valid @RequestBody CauHinhHeThongDto dto){
        return new ResponseEntity<>(cauHinhHeThongService.updateCauHinhHeThong(id, dto),HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 27-05-2021
     * @param phuCap: phụ cấp cần thay đổi
     * @return: Cấu hình hệ thống đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update Lương cộng tác viên")
    @PutMapping("/phu-cap")
    public ResponseEntity<CauHinhHeThong> updatePhuCapCTV(@RequestParam double phuCap){
        return new ResponseEntity<>(cauHinhHeThongService.updatePhuCapCTV(phuCap),HttpStatus.OK);
    }
}
