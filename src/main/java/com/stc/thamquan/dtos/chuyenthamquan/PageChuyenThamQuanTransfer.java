package com.stc.thamquan.dtos.chuyenthamquan;

import com.stc.thamquan.dtos.Total;
import com.stc.thamquan.entities.ChuyenThamQuan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/8/21
 * Time      : 21:32
 * Filename  : PageGiangVienTransfer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageChuyenThamQuanTransfer {
    private List<Total> total;

    private List<ChuyenThamQuan> metaData;
}
