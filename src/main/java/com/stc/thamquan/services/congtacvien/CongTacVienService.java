package com.stc.thamquan.services.congtacvien;


import com.stc.thamquan.dtos.congtacvien.CongTacVienDto;
import com.stc.thamquan.entities.CongTacVien;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 15:14
 * Filename  : CongTacVienService
 */
public interface CongTacVienService {

    Page<CongTacVien> getAllCongTacVienPaging(String search, int page, int size, String sort, String column);

    CongTacVien getCongTacVien(String id);

    CongTacVien getCongTacVienByEmail(String email);

    CongTacVien createCongTacVien(CongTacVienDto dto);

    CongTacVien updateCongTacVien(String id, CongTacVienDto dto);

    CongTacVien changeStatus(String id);

    CongTacVien getCongTacVienByIdCore(String id);

    CongTacVien getCurrent(Principal principal);

    List<CongTacVien> getAllCongTacViensActive(String search);

}
