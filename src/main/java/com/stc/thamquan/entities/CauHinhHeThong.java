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
 * Date      : 4/11/21
 * Time      : 14:28
 * Filename  : CauHinhHeThong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cau-hinh-he-thong")
public class CauHinhHeThong {
    @Id
    private String id;

    @ApiModelProperty(value = "Email dùng để gửi thư")
    private String emailGuiThu;

    @ApiModelProperty(value = "Password email gửi thư")
    private String passwordEmailGuiThu;

    @ApiModelProperty(value = "Email dùng để nhận thư")
    private String emailNhanThu;

    private double phuCapCongTacVien;
}
