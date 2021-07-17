package com.stc.thamquan.dtos.dotthamquan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 16:01
 * Filename  : DotThamQuanDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DotThamQuanDto {
    private String tenDotThamQuan;

    private Date tuNgay;

    private Date denNgay;

    private String namHoc;

    private String hocKy;

    private String mucDich;

    private String yeuCau;

    private String noiDungThamQuan;
}
