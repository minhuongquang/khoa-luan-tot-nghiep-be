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
 * Filename  : HocVi
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hoc-vi")
public class HocVi {
    @Id
    private String id;

    @ApiModelProperty(value = "Tiến sỹ, Thạc sỹ")
    private String tenHocVi;

    @ApiModelProperty(value = "TS, ThS")
    private String tenVietTat = "";

    private boolean trangThai = true;
}
