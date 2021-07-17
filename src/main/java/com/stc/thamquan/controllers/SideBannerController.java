package com.stc.thamquan.controllers;

import com.stc.thamquan.dtos.banner.SideBannerDto;
import com.stc.thamquan.entities.SideBanner;
import com.stc.thamquan.services.sidebanner.SideBannerService;
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
 * User      : quocnt
 * Date      : 06/02/21
 * Filename  : SideBannerController
 */

@RestController
@RequestMapping("/rest/side-banner")
public class SideBannerController {
    private final SideBannerService sideBannerService;

    public SideBannerController(SideBannerService sideBannerService) {
        this.sideBannerService = sideBannerService;
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các side-banner được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Side-Banner Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<SideBanner>> getAllSideBannerPaging(
            @RequestParam(value = "search", required = false, defaultValue = "")String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tieuDe")String column){
        return new ResponseEntity<>(sideBannerService.getAllSideBannerPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param search
     * @return: List side-banner đang active
     */
    @ApiOperation(value = "Get List Side-Banner Active")
    @GetMapping
    public ResponseEntity<List<SideBanner>> getAllSideBannerActive(
            @RequestParam(value = "search", required = false, defaultValue = "")String search) {
        return new ResponseEntity<>(sideBannerService.getAllSideBannerAcive(search), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id
     * @return: side-banner theo id
     */
    @ApiOperation(value = "Get side-banner by id")
    @GetMapping("/{id}")
    public ResponseEntity<SideBanner> getSideBanner(@PathVariable String id){
        return new ResponseEntity<>(sideBannerService.getSideBanner(id), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param dto: SideBannerDto
     * @return: Created Side-Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create new side-banner")
    @PostMapping
    public ResponseEntity<SideBanner> createSideBanner(@Valid @RequestBody SideBannerDto dto){
        return new ResponseEntity<>(sideBannerService.createSideBanner(dto), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id SideBanner can update
     * @param dto: SideBannerDto
     * @return: Updated Side-Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update side-banner")
    @PutMapping("/{id}")
    public ResponseEntity<SideBanner> updateSideBanner(@PathVariable String id, @Valid @RequestBody SideBannerDto dto){
        return new ResponseEntity<>(sideBannerService.updateSideBanner(id, dto), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id SideBanner can change status
     * @return: changed status's Side-Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Change status side-banner")
    @DeleteMapping("/{id}")
    public ResponseEntity<SideBanner> changeStatusSideBanner(@PathVariable String id){
        return new ResponseEntity<>(sideBannerService.changeStatus(id), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id SideBanner can delete
     * @return: deleted side-banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete hoàn toàn side-banner")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SideBanner> deleteSideBanner(@PathVariable String id) {
        return new ResponseEntity<>(sideBannerService.deleteSideBanner(id), HttpStatus.OK);
    }
}
