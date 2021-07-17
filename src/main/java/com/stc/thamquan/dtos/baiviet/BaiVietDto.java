package com.stc.thamquan.dtos.baiviet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:35
 * Filename  : BaiVietDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaiVietDto {
    private int thuTu;

    private String loaiBaiViet;

    private String fileAnhBia;

    private String tieuDe;

    private String tieuDeEn;

    private String noiDung;

    private String noiDungEn;
}
