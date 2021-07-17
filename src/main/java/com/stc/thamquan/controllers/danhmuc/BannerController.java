package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.banner.BannerDto;
import com.stc.thamquan.entities.Banner;
import com.stc.thamquan.services.banner.BannerService;
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
 * Filename  : BannerController
 */

@RestController
@RequestMapping("/rest/banner")
public class BannerController {
    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param search
     * @param page
     * @param size
     * @param sort
     * @param column
     * @return: Tất cả các banner được phân trang
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get All Banner Paging")
    @GetMapping("/paging")
    public ResponseEntity<Page<Banner>> getAllBannerPaging(
            @RequestParam(value = "search", required = false, defaultValue = "")String search,
            @RequestParam(value = "page", required = false, defaultValue = "${paging.default.page}")int page,
            @RequestParam(value = "size", required = false, defaultValue = "${paging.default.size}")int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc")String sort,
            @RequestParam(value = "column", required = false, defaultValue = "tieuDe")String column){
        return new ResponseEntity<>(bannerService.getAllBannerPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param search
     * @return: List banner đang active
     */
    @ApiOperation(value = "Get List Banner Active")
    @GetMapping
    public ResponseEntity<List<Banner>> getAllBannerActive(
            @RequestParam(value = "search", required = false, defaultValue = "")String search) {
        return new ResponseEntity<>(bannerService.getAllBannerAcive(search), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id
     * @return: banner theo id
     */
    @ApiOperation(value = "Get banner by id")
    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBanner(@PathVariable String id) {
        return new ResponseEntity<>(bannerService.getBanner(id), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param dto: BannerDto
     * @return: Created Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create new banner")
    @PostMapping
    public ResponseEntity<Banner> createBanner(@Valid @RequestBody BannerDto dto) {
        return new ResponseEntity<>(bannerService.createBanner(dto), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id Banner can update
     * @param dto: BannerDto
     * @return: Updated Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update banner")
    @PutMapping("/{id}")
    public ResponseEntity<Banner> updateBanner(@PathVariable String id, @Valid @RequestBody BannerDto dto) {
        return new ResponseEntity<>(bannerService.updateBanner(id, dto), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id Banner can change status
     * @return: changed status's Banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Change status banner")
    @DeleteMapping("/{id}")
    public ResponseEntity<Banner> changeStatusBanner(@PathVariable String id) {
        return new ResponseEntity<>(bannerService.changeStatus(id), HttpStatus.OK);
    }

    /***
     * @author: ntquoc
     * @createDate: 02-06-2021
     * @param id: id Banner can delete
     * @return: deleted banner
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete hoàn toàn banner")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Banner> deleteBanner(@PathVariable String id) {
        return new ResponseEntity<>(bannerService.deleteBanner(id), HttpStatus.OK);
    }
}