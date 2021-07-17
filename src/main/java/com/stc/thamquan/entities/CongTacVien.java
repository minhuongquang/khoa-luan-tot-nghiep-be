package com.stc.thamquan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stc.thamquan.utils.EnumRole;
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
 * User      : truc
 * Date      : 6/4/21
 * Time      : 14:54
 * Filename  : CongTacVien
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cong-tac-vien")
public class CongTacVien {
    @Id
    private String id;

    private String maSV;

    private String email;

    @JsonIgnore
    private String password;

    private String hoTen;

    private String dienThoai;

    private List<String> roles = new ArrayList<>();

    private boolean trangThai = true;
}
