package com.stc.thamquan.services.taikhoan;

import com.stc.thamquan.dtos.taikhoan.TaiKhoanDto;
import com.stc.thamquan.entities.TaiKhoan;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:14
 * Filename  : TaiKhoanService
 */
public interface TaiKhoanService {

    List<String> getRoleTaiKhoans();

    Page<TaiKhoan> getAllTaiKhoanPaging(String search, int page, int size, String sort, String column);

    TaiKhoan getTaiKhoan(String id);

    TaiKhoan getTaiKhoanByEmail(String email);

    TaiKhoan createTaiKhoan(TaiKhoanDto dto);

    TaiKhoan updateTaiKhoan(String id, TaiKhoanDto dto);

    TaiKhoan changeStatus(String id);
    
    TaiKhoan getTaiKhoanByIdCore(String id);

    TaiKhoan getCurrent(Principal principal);
}
