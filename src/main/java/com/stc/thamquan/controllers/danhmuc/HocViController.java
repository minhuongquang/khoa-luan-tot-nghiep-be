package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.hocvi.HocViDto;
import com.stc.thamquan.entities.HocVi;
import com.stc.thamquan.services.hocvi.HocViService;
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
 * Time      : 14:52
 * Filename  : HocViController
 */
@RestController
@RequestMapping("/rest/hoc-vi")
public class HocViController {
    private final HocViService hocViService;

    public HocViController(HocViService hocViService) {
        this.hocViService = hocViService;
    }


    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các học vị được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Học Vị Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<HocVi>>getAllHocViPaging(
            @RequestParam(value = "search", required = false, defaultValue = "")String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tenHocVi")String column){
        return  new ResponseEntity<>(hocViService.getAllHocViPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param search
     * @return: Danh sách các học vị active
     */
    @ApiOperation(value = "Get list học vị active (trangThai: true)")
    @GetMapping
    public ResponseEntity<List<HocVi>>getAllHocViActive(
            @RequestParam(value = "search", required = false, defaultValue = "")String search){
        return new ResponseEntity<>(hocViService.getAllHocVis(search), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id học vị cần tim
     * @return: Học vị muốn tìm theo id
     */
    @ApiOperation(value = "Get học vị by id")
    @GetMapping("/{id}")
    public ResponseEntity<HocVi>getHocVi(@PathVariable String id){
        return new ResponseEntity<>(hocViService.getHocVi(id), HttpStatus.OK);
    }


    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param hocViDto: DTO dùng để tạo học vị
     * @return: Học vị đã tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create Học vị")
    @PostMapping
    public ResponseEntity<HocVi>createHocVi(@Valid @RequestBody HocViDto hocViDto){
        return new ResponseEntity<>(hocViService.createHocVi(hocViDto), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id học vị cần update
     * @param hocViDto: DTO dùng để update học vị
     * @return
     */
    @ApiOperation(value = "Update Học vị")
    @PutMapping("/{id}")
    public ResponseEntity<HocVi>updateHocVi(@PathVariable String id ,@Valid @RequestBody HocViDto hocViDto){
        return new ResponseEntity<>(hocViService.updateHocVi(id, hocViDto), HttpStatus.OK);
    }

    /***
     * @author: vlong
     * @createDate: 06-04-2021
     * @param id: Id học vị cần update
     * @return:
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete Học vị")
    @DeleteMapping("/{id}")
    public ResponseEntity<HocVi>deleteHocVi(@PathVariable String id){
        return new ResponseEntity<>(hocViService.changeStatus(id),HttpStatus.OK);
    }

}
