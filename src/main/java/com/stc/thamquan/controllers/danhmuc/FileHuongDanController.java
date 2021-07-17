package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.filehuongdan.FileHuongDanDto;
import com.stc.thamquan.entities.FileHuongDan;
import com.stc.thamquan.services.filehuongdan.FileHuongDanService;
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
 * User      : truc
 * Date      : 06/11/21
 * Time      : 14:53
 * Filename  : FileHuongDanController
 */
@RestController
@RequestMapping("/rest/file-huong-dan")
public class FileHuongDanController {

    private final FileHuongDanService fileHuongDanService;

    public FileHuongDanController(FileHuongDanService fileHuongDanService) {
        this.fileHuongDanService = fileHuongDanService;
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các file hướng dẫn được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All file hướng dẫn Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<FileHuongDan>> getAllFileHuongDanPaging(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tenFileHuongDan") String column) {
        return new ResponseEntity<>(fileHuongDanService.getAllFileHuongDanPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param search
     * @return: Danh sách các file hướng dẫn active
     */
    @ApiOperation(value = "Get list file hướng dẫn active (trangThai: true)")
    @GetMapping
    public ResponseEntity<List<FileHuongDan>> getAllFileHuongDanActive(
            @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(fileHuongDanService.getAllFileHuongDanActive(search), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 06-04-2021
     * @param id: Id file hướng dẫn cần tim
     * @return: file hướng dẫn muốn tìm theo id
     */
    @ApiOperation(value = "Get file hướng dẫn by id")
    @GetMapping("/{id}")
    public ResponseEntity<FileHuongDan> getFileHuongDan(@PathVariable String id) {
        return new ResponseEntity<>(fileHuongDanService.getFileHuongDan(id), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param dto
     * @return: file hướng dẫn đã tạo thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create file hướng dẫn")
    @PostMapping
    public ResponseEntity<FileHuongDan> createFileHuongDan(@Valid @RequestBody FileHuongDanDto dto) {
        return new ResponseEntity<>(fileHuongDanService.createFileHuongDan(dto), HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param id: id file hướng dẫn muốn update
     * @param dto: dto cuả file hướng dẫn
     * @return: file hướng dẫn đã cập nhật thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update file hướng dẫn")
    @PutMapping("/{id}")
    public ResponseEntity<FileHuongDan> updateFileHuongDan(@PathVariable String id,
                                                           @Valid @RequestBody FileHuongDanDto dto){
        return new ResponseEntity<>(fileHuongDanService.updateFileHuongDan(id,dto),HttpStatus.OK);
    }

    /***
     * @author: truc
     * @createDate: 11-06-2021
     * @param id: id file hướng dẫn muốn thay đổi trạng thái
     * @return: file hướng dẫn đã thay đổi trạng thái thành công
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete file hướng dẫn")
    @DeleteMapping("/{id}")
    public ResponseEntity<FileHuongDan> changeStatus(@PathVariable String id) {
        return new ResponseEntity<>(fileHuongDanService.changeStatus(id), HttpStatus.OK);
    }
}
