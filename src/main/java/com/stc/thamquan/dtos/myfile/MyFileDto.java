package com.stc.thamquan.dtos.myfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : hung
 * Date      : 3/31/21
 * Time      : 10:05
 * Filename  : MyFileDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyFileDto {

    private String tenFile;

    private String duongDan;

    private long kichThuoc;

    private String loaiFile;
}
