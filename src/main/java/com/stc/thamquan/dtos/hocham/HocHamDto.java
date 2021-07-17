package com.stc.thamquan.dtos.hocham;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/2/21
 * Time      : 11:38
 * Filename  : HocHamDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HocHamDto {
    private String tenHocHam;

    private String tenVietTat;

    private boolean show;
}
