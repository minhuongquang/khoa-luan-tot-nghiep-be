package com.stc.thamquan.dtos.thongke;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 10:00
 * Filename  : CauTraLoi
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CauTraLoi {
    private String dapAn;

    private List<ThuTuUuTien> thuTuUuTiens;
}
