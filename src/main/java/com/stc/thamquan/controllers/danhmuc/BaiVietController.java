package com.stc.thamquan.controllers.danhmuc;


import com.stc.thamquan.dtos.baiviet.BaiVietDto;
import com.stc.thamquan.entities.BaiViet;
import com.stc.thamquan.services.baiviet.BaiVietService;
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
 * Date      : 5/6/21
 * Time      : 15:28
 * Filename  : BaiVietController
 */
@RestController
@RequestMapping("/rest/bai-viet")
public class BaiVietController {
    private final BaiVietService baiVietService;

    public BaiVietController(BaiVietService baiVietService) {
        this.baiVietService = baiVietService;
    }

    @GetMapping("/loai-bai-viet")
    public ResponseEntity<List<String>> getLoaiBaiViets() {
        return new ResponseEntity<>(baiVietService.getLoaiBaiViets(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @ApiOperation(value = "Get danh sách bài viết filter admin")
    @GetMapping("/filter")
    public ResponseEntity<Page<BaiViet>> filter(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "loaiBaiViet", required = false, defaultValue = "") String loaiBaiViet,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "trangThai") String column) {
        return new ResponseEntity<>(baiVietService.filter(search, loaiBaiViet, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get danh sách bài viết paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<BaiViet>> paging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "loaiBaiViet", required = false, defaultValue = "") String loaiBaiViet,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "tieuDe") String column) {
        return new ResponseEntity<>(baiVietService.paging(search, loaiBaiViet, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get bài viết by id")
    @GetMapping("/{id}")
    public ResponseEntity<BaiViet> getBaiViet(@PathVariable String id) {
        return new ResponseEntity<>(baiVietService.getBaiViet(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Create bài viết")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @PostMapping
    public ResponseEntity<BaiViet> createBaiViet(@Valid @RequestBody BaiVietDto dto) {
        return new ResponseEntity<>(baiVietService.createBaiViet(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Update bài viết")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @PutMapping("/{id}")
    public ResponseEntity<BaiViet> updateBaiViet(@PathVariable String id, @Valid @RequestBody BaiVietDto dto) {
        return new ResponseEntity<>(baiVietService.updateBaiViet(id, dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Change status")
    @PreAuthorize("hasAnyRole('ADMIN', 'THU_KY_KHOA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaiViet> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(baiVietService.changeStatus(id), HttpStatus.OK);
    }
}
