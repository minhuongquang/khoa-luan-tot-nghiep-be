package com.stc.thamquan.dtos.baiviet;

import com.stc.thamquan.dtos.Total;
import com.stc.thamquan.entities.BaiViet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:53
 * Filename  : PageBaiVietTransfer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageBaiVietTransfer {
    private List<Total> total;

    private List<BaiViet> metaData;
}
