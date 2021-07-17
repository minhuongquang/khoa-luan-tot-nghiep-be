package com.stc.thamquan.services.doanhnghiep;


import com.stc.thamquan.dtos.doanhnghiep.DoanhNghiepDto;
import com.stc.thamquan.entities.DoanhNghiep;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 4/14/21
 * Time      : 16:08
 * Filename  : DoanhNghiepService
 */

public interface DoanhNghiepService {

    Page<DoanhNghiep> getAllDoanhNghiepsPaging(String search, int page, int size, String sort ,String column);

    List<DoanhNghiep> getAllDoanhNghiepsActive(String search);

    DoanhNghiep getDoanhNghiep(String id);

    DoanhNghiep getDoanhNghiepByIdCore(String id);

    DoanhNghiep createDoanhNghiep(DoanhNghiepDto dto);

    DoanhNghiep updateDoanhNghiep(String id, DoanhNghiepDto dto);

    DoanhNghiep changeStatus(String id);

    void importDanhSachDoanhNghieps( MultipartFile file) throws Exception;

    DoanhNghiep getCurrentDoanhNghiep(Principal principal);

}
