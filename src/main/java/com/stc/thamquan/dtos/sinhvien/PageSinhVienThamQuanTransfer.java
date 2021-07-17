package com.stc.thamquan.dtos.sinhvien;

import com.stc.thamquan.dtos.Total;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
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
 * Filename  : PageSinhVienThamQuanTransfer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageSinhVienThamQuanTransfer {
    private List<Total> total;

    private List<SinhVienThamQuan> metaData;
}
