package com.stc.thamquan.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/29/21
 * Time      : 2:02 PM
 * Filename  : HocHam
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hoc-ham")
public class HocHam {
    @Id
    private String id;

    @ApiModelProperty(value = "Giáo sư, Phó Giáo sư")
    private String tenHocHam;

    @ApiModelProperty(value = "GS, PGS")
    private String tenVietTat = "";

    private boolean show = true;

    private boolean trangThai = true;
}
