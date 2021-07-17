package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.giangvien.FilterGiangVienDto;
import com.stc.thamquan.dtos.giangvien.GiangVienDto;
import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.services.giangvien.GiangVienService;
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
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:51
 * Filename  : GiangVienController
 */
@RestController
@RequestMapping("/rest/giang-vien")
public class GiangVienController {
    private final GiangVienService giangVienService;

    public GiangVienController(GiangVienService giangVienService) {
        this.giangVienService = giangVienService;
    }


    /***
     * @author: thangpx
     * @createdDate: 08/04/2021
     * @param dto: DTO filter thông tin giảng viên
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Danh sách giảng viên đã được phân trang
     */
    @ApiOperation(value = "Filter giảng viên theo mã, email, họ tên, khoa, ngành, lĩnh vực nghiên cứu")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @PostMapping("/filter")
    public ResponseEntity<Page<GiangVien>> filter(@Valid @RequestBody FilterGiangVienDto dto,
                                                  @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
                                                  @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
                                                  @RequestParam(name = "column", required = false, defaultValue = "maGiangVien") String column) {

        return new ResponseEntity<>(giangVienService.filter(dto, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param id: Id giảng viên cần lấy thông tin
     * @return: Giảng viên có id tương ứng trong cơ sở dữ liệu
     */
    @ApiOperation(value = "Get giảng viên by id")
    @GetMapping("/{id}")
    public ResponseEntity<GiangVien> getGiangVien(@PathVariable String id) {
        return new ResponseEntity<>(giangVienService.getGiangVien(id), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param principal: token của giảng viên đăng login vào hệ thống
     * @return: Giảng viên đang đăng nhập vào hệ thống
     */
    @ApiOperation(value = "Get giảng viên đang đăng nhập by token")
    @GetMapping("/current")
    public ResponseEntity<GiangVien> getGiangVien(Principal principal) {
        return new ResponseEntity<>(giangVienService.getGiangVien(principal), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param dto : DTO tạo mới giảng viên
     * @param principal: token của người tạo
     * @return: Giảng viên được tạo thành công
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @ApiOperation(value = "Create giảng viên")
    @PostMapping
    public ResponseEntity<GiangVien> createGiangVien(@Valid @RequestBody GiangVienDto dto, Principal principal) {
        return new ResponseEntity<>(giangVienService.createGiangVien(dto, principal), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param id: Id giảng viên cần cập nhật thông tin
     * @param dto: DTO cập nhật thông tin giảng viên (ko cho cập nhật email)
     * @param principal: token người update
     * @return: Giảng viên được update thông tin thành công
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @ApiOperation(value = "Update giảng viên")
    @PutMapping("/{id}")
    public ResponseEntity<GiangVien> updateGiangVien(@PathVariable String id, @Valid @RequestBody GiangVienDto dto, Principal principal) {
        return new ResponseEntity<>(giangVienService.updateGiangVien(id, dto, principal), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createdDate: 09/04/2021
     * @param id: Id giảng viên cần thay đổi trạng thái
     * @return: Giảng viên có id tương ứng đã được thay đổi trạng thái
     */
    @ApiOperation(value = "Thay đổi trạng thái giảng viên")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<GiangVien> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(giangVienService.changeStatus(id), HttpStatus.OK);
    }


    /***
     * @author: truc
     * @createdDate: 06/06/2021
     * @param search: tìm kiếm theo mã, họ tên giảng viên
     * @return: danh sách giảng viên
     */
    @ApiOperation(value = "Get all giảng viên active")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @GetMapping()
    public ResponseEntity<List<GiangVien>> getAllGiangViensActive(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(giangVienService.getAllGiangViensActive(search), HttpStatus.OK);
    }

}
