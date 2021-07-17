package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/27/21
 * Time      : 10:16
 * Filename  : LichSuChuyenThamQuan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "lich-su-chuyen-tham-quan")
public class LichSuChuyenThamQuan {
    @Id
    private String id;

    private ChuyenThamQuan chuyenThamQuanCu;

    private ChuyenThamQuan chuyenThamQuanMoi;

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;
}
