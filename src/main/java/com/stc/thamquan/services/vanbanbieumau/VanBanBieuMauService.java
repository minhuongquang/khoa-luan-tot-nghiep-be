package com.stc.thamquan.services.vanbanbieumau;


import com.stc.thamquan.dtos.vanbanbieumau.VanBanBieuMauDto;
import com.stc.thamquan.entities.VanBanBieuMau;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/11/21
 * Time      : 15:14
 * Filename  : VanBanBieuMauService
 */
public interface VanBanBieuMauService {
    Page<VanBanBieuMau> getAllVanBanBieuMauPaging(String search, int page, int size, String sort, String column);

    List<VanBanBieuMau> getAllVanBanBieuMauActive(String search);

    VanBanBieuMau getVanBanBieuMau(String id);

    VanBanBieuMau getVanBanBieuMauByIdCore(String id);

    VanBanBieuMau createVanBanBieuMau(VanBanBieuMauDto dto);

    VanBanBieuMau updateVanBanBieuMau(String id, VanBanBieuMauDto dto);

    VanBanBieuMau changeStatus(String id);

}
