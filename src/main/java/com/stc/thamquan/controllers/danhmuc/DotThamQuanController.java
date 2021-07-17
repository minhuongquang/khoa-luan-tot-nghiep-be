package com.stc.thamquan.controllers.danhmuc;


import com.stc.thamquan.dtos.dotthamquan.DotThamQuanDto;
import com.stc.thamquan.entities.DotThamQuan;
import com.stc.thamquan.services.dotthamquan.DotThamQuanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 4/14/21
 * Time      : 10:55
 * Filename  : DotThamQuanController
 */

@RestController
@RequestMapping("/rest/dot-tham-quan")
public class DotThamQuanController {
    private final DotThamQuanService dotThamQuanService;

    public DotThamQuanController(DotThamQuanService dotThamQuanService) {
        this.dotThamQuanService = dotThamQuanService;
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
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get all đợt tham quan paging admin")
    @GetMapping("/paging")
    public ResponseEntity<Page<DotThamQuan>> getAllDotThamQuansPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tenDotThamQuan") String column) {
        return new ResponseEntity<>(dotThamQuanService.getAllDotThamQuansPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @lastModified:
     * @changeBy:
     * @lastChange:
     * @return
     */
    @ApiOperation(value = "Get danh sách đợt tham quan active")
    @GetMapping
    public ResponseEntity<Page<DotThamQuan>> getDotThamQuanActive(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                  @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
                                                                  @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
                                                                  @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
                                                                  @RequestParam(name = "column", required = false, defaultValue = "tenDotThamQuan") String column) {
        return new ResponseEntity<>(dotThamQuanService.getAllDotThamQuanActive(search,page,size,sort,column), HttpStatus.OK);
    }


    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id đợt tham quan cần lấy
     * @return: Đợt tham quan tìm thấy trong db
     */
    @ApiOperation(value = "Get đợt tham quan by id")
    @GetMapping("/{id}")
    public ResponseEntity<DotThamQuan> getDotThamQuan(@PathVariable String id) {
        return new ResponseEntity<>(dotThamQuanService.getDotThamQuan(id), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param dto: DTO tạo đợt tham quan
     * @return: Đợt tham quan đã được tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create đợt tham quan")
    @PostMapping
    public ResponseEntity<DotThamQuan> createDotThamQuan(@Valid @RequestBody DotThamQuanDto dto) {
        return new ResponseEntity<>(dotThamQuanService.createDotThamQuan(dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id đợt tham quan cần update
     * @param dto: DTO update đợt tham quan
     * @return: Đợt tham quan đã được update
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update đợt tham quan")
    @PutMapping("/{id}")
    public ResponseEntity<DotThamQuan> updateDotThamQuan(@PathVariable String
                                                                 id, @Valid @RequestBody DotThamQuanDto dto) {
        return new ResponseEntity<>(dotThamQuanService.updateDotThamQuan(id, dto), HttpStatus.OK);
    }

    /***
     * @author: trucntt
     * @createDate: 13-04-2021
     * @param id: Id đợt tham quan cần thay đổi trạng thái
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "change status đợt tham quan")
    @DeleteMapping("/{id}")
    public ResponseEntity<DotThamQuan> changStatus(@PathVariable String id) {
        return new ResponseEntity<>(dotThamQuanService.changeStatus(id), HttpStatus.OK);
    }


}
