package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.hocham.HocHamDto;
import com.stc.thamquan.entities.HocHam;
import com.stc.thamquan.services.hocham.HocHamService;
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
 * Time      : 14:31
 * Filename  : HocHamController
 */
@RestController
@RequestMapping("/rest/hoc-ham")
public class HocHamController {
    private final HocHamService hocHamService;

    public HocHamController(HocHamService hocHamService) {
        this.hocHamService = hocHamService;
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @param search: Từ khóa tìm kiếm học hàm theo tên, tên tiếng anh, tên viết tắt
     * @return
     */
    @ApiOperation(value = "Get danh sách học hàm active")
    @GetMapping
    public ResponseEntity<List<HocHam>> getAllHocHams(@RequestParam(name = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(hocHamService.getAllHocHams(search), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all học hàm phân trang cho admin")
    @GetMapping("/paging")
    public ResponseEntity<Page<HocHam>> getAllHocHamsPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenHocHam") String column) {
        return new ResponseEntity<>(hocHamService.getAllHocHamPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @param id: Id học hàm cần lấy
     * @return: Học hàm tìm thấy trong db
     */
    @ApiOperation(value = "Get học hàm by id")
    @GetMapping("/{id}")
    public ResponseEntity<HocHam> getHocHam(@PathVariable String id){
        return new ResponseEntity<>(hocHamService.getHocHam(id),HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @param dto: DTO tạo học hàm
     * @return: Học hàm đã được tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create học hàm")
    @PostMapping
    public ResponseEntity<HocHam> createHocHam(@Valid @RequestBody HocHamDto dto){
        return new ResponseEntity<>(hocHamService.createHocHam(dto),HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @param id: Id học hàm cần update
     * @param dto: DTO update học hàm
     * @return: Học hàm đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update học hàm")
    @PutMapping("/{id}")
    public ResponseEntity<HocHam> updateHocHam(@PathVariable String id, @Valid @RequestBody HocHamDto dto){
        return new ResponseEntity<>(hocHamService.updateHocHam(id, dto),HttpStatus.OK);
    }

    /***
     * @author: thangpx
     * @createDate: 05-04-2021
     * @param id: Id học hàm cần thay đổi trạng thái
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status học hàm")
    @DeleteMapping("/{id}")
    public ResponseEntity<HocHam> changStatus(@PathVariable String id){
        return new ResponseEntity<>(hocHamService.changeStatus(id),HttpStatus.OK);
    }
}
