package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.khoa.KhoaDto;
import com.stc.thamquan.dtos.vanbanbieumau.VanBanBieuMauDto;
import com.stc.thamquan.entities.Khoa;
import com.stc.thamquan.entities.VanBanBieuMau;
import com.stc.thamquan.services.vanbanbieumau.VanBanBieuMauService;
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
 * User      : truc
 * Date      : 06/11/21
 * Time      : 14:53
 * Filename  : VanBanBieuMauController
 */
@RestController
@RequestMapping("/rest/van-ban-bieu-mau")
public class VanBanBieuMauController {

    private final VanBanBieuMauService vanBanBieuMauService;

    public VanBanBieuMauController(VanBanBieuMauService vanBanBieuMauService) {
        this.vanBanBieuMauService = vanBanBieuMauService;
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các văn bản, biểu mẫu được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Văn Bản, Biểu Mẫu Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<VanBanBieuMau>> getAllVanBanBieuMauPaging(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tenVanBanBieuMau") String column) {
        return new ResponseEntity<>(vanBanBieuMauService.getAllVanBanBieuMauPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param search
     * @return: Danh sách các văn bản, biểu mẫu active
     */
    @ApiOperation(value = "Get list văn bản, biểu mẫu active (trangThai: true)")
    @GetMapping
    public ResponseEntity<List<VanBanBieuMau>> getAllVanBanBieuMauActive(
            @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(vanBanBieuMauService.getAllVanBanBieuMauActive(search), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 06-04-2021
     * @param id: Id văn bản, biểu mẫu cần tim
     * @return: văn bản, biểu mẫu muốn tìm theo id
     */
    @ApiOperation(value = "Get văn bản, biểu mẫu by id")
    @GetMapping("/{id}")
    public ResponseEntity<VanBanBieuMau> getVanBanBieuMau(@PathVariable String id) {
        return new ResponseEntity<>(vanBanBieuMauService.getVanBanBieuMau(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param dto
     * @return: văn bản, biểu mẫu đã tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create văn bản, biểu mẫu")
    @PostMapping
    public ResponseEntity<VanBanBieuMau> createVanBanBieuMau(@Valid @RequestBody VanBanBieuMauDto dto) {
        return new ResponseEntity<>(vanBanBieuMauService.createVanBanBieuMau(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param id: id văn bản biểu mẫu muốn update
     * @param dto: dto cuả văn bản biểu mẫu
     * @return: văn bản, biểu mẫu đã cập nhật thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update văn bản, biểu mẫu")
    @PutMapping("/{id}")
    public ResponseEntity<VanBanBieuMau> updateVanBanBieuMau(@PathVariable String id,
                                                             @Valid @RequestBody VanBanBieuMauDto dto){
        return new ResponseEntity<>(vanBanBieuMauService.updateVanBanBieuMau(id,dto),HttpStatus.OK);
}

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param id: id văn bản biểu mẫu muốn thay đổi trạng thái
     * @return: văn bản, biểu mẫu đã thay đổi trạng thái thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete văn bản, biểu mẫu")
    @DeleteMapping("/{id}")
    public ResponseEntity<VanBanBieuMau> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(vanBanBieuMauService.changeStatus(id), HttpStatus.OK);
    }
}
