package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.linhvuc.LinhVucDto;
import com.stc.thamquan.entities.LinhVuc;
import com.stc.thamquan.services.linhvuc.LinhVucService;
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
 * Time      : 15:37
 * Filename  : LinhVucController
 */
@RestController
@RequestMapping("/rest/linh-vuc")
public class LinhVucController {
    private final LinhVucService linhVucService;

    public LinhVucController(LinhVucService linhVucService) {
        this.linhVucService = linhVucService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Linh Vuc Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<LinhVuc>> getAllLinhVucPaging(
            @RequestParam(value = "search", required = false, defaultValue = "")String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tenLinhVuc")String column){
        return  new ResponseEntity<>(linhVucService.getAllLinhVucPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list linh vuc active (trangThai: true)")
    @GetMapping
    public ResponseEntity<List<LinhVuc>>getAllLinhVucActive(
            @RequestParam(value = "search", required = false, defaultValue = "")String search){
        return new ResponseEntity<>(linhVucService.getLinhVucs(search), HttpStatus.OK);
    }

    @ApiOperation(value = "Get linh vuc by id")
    @GetMapping("/{id}")
    public ResponseEntity<LinhVuc>getLinhVuc(@PathVariable String id){
        return new ResponseEntity<>(linhVucService.getLinhVuc(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create linh vuc")
    @PostMapping
    public ResponseEntity<LinhVuc>createLinhVuc(@Valid @RequestBody LinhVucDto linhVucDto){
        return new ResponseEntity<>(linhVucService.createLinhVuc(linhVucDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update linh vuc")
    @PutMapping("/{id}")
    public ResponseEntity<LinhVuc>updateLinhVuc(@PathVariable String id ,@Valid @RequestBody LinhVucDto linhVucDto){
        return new ResponseEntity<>(linhVucService.updateLinhVuc(id, linhVucDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete lĩnh vực")
    @DeleteMapping("/{id}")
    public ResponseEntity<LinhVuc>deleteLinhVuc(@PathVariable String id){
        return new ResponseEntity<>(linhVucService.changeStatus(id),HttpStatus.OK);
    }


}
