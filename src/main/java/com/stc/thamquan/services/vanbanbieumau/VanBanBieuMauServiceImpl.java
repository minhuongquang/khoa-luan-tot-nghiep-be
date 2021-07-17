package com.stc.thamquan.services.vanbanbieumau;

import com.stc.thamquan.dtos.vanbanbieumau.VanBanBieuMauDto;
import com.stc.thamquan.entities.MyFile;
import com.stc.thamquan.entities.VanBanBieuMau;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.VanBanBieuMauRepository;
import com.stc.thamquan.services.myfile.MyFileService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/11/21
 * Time      : 18:08
 * Filename  : VanBanBieuMauServiceImpl
 */
@Slf4j
@Service
public class VanBanBieuMauServiceImpl implements VanBanBieuMauService {

    private final VanBanBieuMauRepository vanBanBieuMauRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final MyFileService myFileService;

    public VanBanBieuMauServiceImpl(VanBanBieuMauRepository vanBanBieuMauRepository, VietnameseStringUtils vietnameseStringUtils, MyFileService myFileService) {
        this.vanBanBieuMauRepository = vanBanBieuMauRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.myFileService = myFileService;
    }


    @Override
    public Page<VanBanBieuMau> getAllVanBanBieuMauPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return vanBanBieuMauRepository.getAllVanBanBieuMauPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<VanBanBieuMau> getAllVanBanBieuMauActive(String search) {
        return vanBanBieuMauRepository.getAllVanBanBieuMauActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public VanBanBieuMau getVanBanBieuMau(String id) {
        return vanBanBieuMauRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Văn bản/ Biểu mẫu có id %s không tồn tại", id)));
    }

    @Override
    public VanBanBieuMau getVanBanBieuMauByIdCore(String id) {
        return vanBanBieuMauRepository.findVanBanBieuMauByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public VanBanBieuMau createVanBanBieuMau(VanBanBieuMauDto dto) {
        VanBanBieuMau vanBanBieuMau = new VanBanBieuMau();

        if (ObjectUtils.isEmpty(dto.getTenVanBanBieuMau())) {
            throw new InvalidException("Tên văn bản, biểu mẫu không được bỏ trống");
        }
        if (!ObjectUtils.isEmpty(dto.getTenVanBanBieuMauEn())) {
            vanBanBieuMau.setTenVanBanBieuMauEn(dto.getTenVanBanBieuMauEn());
        }
        if (ObjectUtils.isEmpty(dto.getFileVanBanBieuMau())) {
            throw new InvalidException("File không được bỏ trống");
        }
        vanBanBieuMau.setTenVanBanBieuMau(dto.getTenVanBanBieuMau());
        vanBanBieuMau.setFileVanBanBieuMau(dto.getFileVanBanBieuMau());
        vanBanBieuMau.setTrangThai(true);
        vanBanBieuMauRepository.save(vanBanBieuMau);
        return vanBanBieuMau;
    }

    @Override
    public VanBanBieuMau updateVanBanBieuMau(String id, VanBanBieuMauDto dto) {
        VanBanBieuMau vanBanBieuMau = getVanBanBieuMau(id);

        if (ObjectUtils.isEmpty(dto.getTenVanBanBieuMau())) {
            throw new InvalidException("Tên văn bản, biểu mẫu không được bỏ trống");
        }
        if (!ObjectUtils.isEmpty(dto.getTenVanBanBieuMauEn())) {
            vanBanBieuMau.setTenVanBanBieuMauEn(dto.getTenVanBanBieuMauEn());
        }
        if (ObjectUtils.isEmpty(dto.getFileVanBanBieuMau())) {
            throw new InvalidException("File không được bỏ trống");
        }
        vanBanBieuMau.setTenVanBanBieuMau(dto.getTenVanBanBieuMau());
        MyFile myFile = myFileService.getFileInfo(dto.getFileVanBanBieuMau());
        vanBanBieuMau.setFileVanBanBieuMau(dto.getFileVanBanBieuMau());
        vanBanBieuMauRepository.save(vanBanBieuMau);
        return vanBanBieuMau;
    }

    @Override
    public VanBanBieuMau changeStatus(String id) {
        VanBanBieuMau vanBanBieuMau = getVanBanBieuMau(id);
        vanBanBieuMau.setTrangThai(!vanBanBieuMau.isTrangThai());
        vanBanBieuMauRepository.save(vanBanBieuMau);
        return vanBanBieuMau;
    }
}
