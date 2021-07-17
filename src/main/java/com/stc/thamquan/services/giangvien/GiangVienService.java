package com.stc.thamquan.services.giangvien;

import com.stc.thamquan.dtos.giangvien.FilterGiangVienDto;
import com.stc.thamquan.dtos.giangvien.GiangVienDto;
import com.stc.thamquan.entities.GiangVien;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:42
 * Filename  : GiangVienService
 */
public interface GiangVienService {


    GiangVien getCurrentGiangVien(String email);

    Page<GiangVien> filter(FilterGiangVienDto dto, int page, int size, String sort, String column);

    GiangVien getGiangVien(String id);

    GiangVien getGiangVien(Principal principal);

    GiangVien getGiangVienByIdCore(String id);

    GiangVien createGiangVien(GiangVienDto dto, Principal principal);

    GiangVien updateGiangVien(String id, GiangVienDto dto, Principal principal);

    GiangVien changeStatus(String id);

    List<GiangVien> getAllGiangViensActive(String search);

}
