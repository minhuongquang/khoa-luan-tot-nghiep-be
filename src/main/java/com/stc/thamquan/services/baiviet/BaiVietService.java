package com.stc.thamquan.services.baiviet;

import com.stc.thamquan.dtos.baiviet.BaiVietDto;
import com.stc.thamquan.entities.BaiViet;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:26
 * Filename  : BaiVietService
 */
public interface BaiVietService {

    List<String> getLoaiBaiViets();

    Page<BaiViet> filter(String search, String loaiBaiViet, int page, int size, String sort, String column);

    Page<BaiViet> paging(String search, String loaiBaiViet, int page, int size, String sort, String column);

    BaiViet getBaiViet(String id);

    BaiViet createBaiViet(BaiVietDto dto);

    BaiViet updateBaiViet(String id, BaiVietDto dto);

    BaiViet changeStatus(String id);
}
