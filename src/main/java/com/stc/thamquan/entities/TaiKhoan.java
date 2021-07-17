package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:54
 * Filename  : TaiKhoan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tai-khoan")
public class TaiKhoan {
    @Id
    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private String hoTen;

    private List<String> roles = new ArrayList<>();

    private boolean trangThai = true;
}
