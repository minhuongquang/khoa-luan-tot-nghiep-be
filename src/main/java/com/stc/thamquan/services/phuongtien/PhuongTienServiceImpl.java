package com.stc.thamquan.services.phuongtien;

import com.stc.thamquan.dtos.phuongtien.PhuongTienDto;
import com.stc.thamquan.entities.DiaDiem;
import com.stc.thamquan.entities.PhuongTien;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.PhuongTienRepository;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 18:07
 * Filename  : PhuongTienServiceImpl
 */
@Slf4j
@Service
public class PhuongTienServiceImpl implements PhuongTienService {
    private final PhuongTienRepository phuongTienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final MessageSource messageSource;

    public PhuongTienServiceImpl(PhuongTienRepository phuongTienRepository, VietnameseStringUtils vietnameseStringUtils, MessageSource messageSource) {
        this.phuongTienRepository = phuongTienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.messageSource = messageSource;
    }

    @Override
    public Page<PhuongTien> getAllPhuongTiensPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return phuongTienRepository.getAllPhuongTiensPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<PhuongTien> getAllPhuongTiensActive(String search) {
        return phuongTienRepository.getAllPhuongTiensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public PhuongTien getPhuongTien(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return phuongTienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Ph????ng ti???n c?? id %s kh??ng t???n t???i", id)));
    }

    @Override
    public PhuongTien getPhuongTienByIdCore(String id) {
        return phuongTienRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public PhuongTien createPhuongTien(PhuongTienDto dto) {
        PhuongTien phuongTien = new PhuongTien();

        if (ObjectUtils.isEmpty(dto.getSoThuTu())) {
            throw new InvalidException("S??? th??? t??? ph????ng ti???n kh??ng ???????c b??? tr???ng");
        }
        if (ObjectUtils.isEmpty(dto.getTenXe())) {
            throw new InvalidException("T??n xe kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getBienSo())) {
            throw new InvalidException("Bi???n s??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getMauXe())) {
            throw new InvalidException("M??u xe kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getSoChoNgoi())) {
            throw new InvalidException("S??? ch??? ng???i kh??ng ???????c b??? tr???ng");
        }

        if (phuongTienRepository.existsByBienSoIgnoreCase(dto.getBienSo())) {
            throw new InvalidException("Ph????ng ti???n ???? t???n t???i");
        }

        phuongTien.setSoThuTu(dto.getSoThuTu());
        phuongTien.setTenXe(dto.getTenXe());
        phuongTien.setBienSo(dto.getBienSo());
        phuongTien.setMauXe(dto.getMauXe());
        phuongTien.setSoChoNgoi(dto.getSoChoNgoi());
        phuongTienRepository.save(phuongTien);
        return phuongTien;
    }

    @Override
    public PhuongTien updatePhuongTien(String id, PhuongTienDto dto) {
        if (ObjectUtils.isEmpty(dto.getSoThuTu())) {
            throw new InvalidException("S??? th??? t??? ph????ng ti???n kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getTenXe())) {
            throw new InvalidException("T??n xe kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getBienSo())) {
            throw new InvalidException("Bi???n s??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getMauXe())) {
            throw new InvalidException("M??u xe kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getSoChoNgoi())) {
            throw new InvalidException("S??? ch??? ng???i kh??ng ???????c b??? tr???ng");
        }

        if (phuongTienRepository.existsByBienSoIgnoreCase(dto.getBienSo())) {
            throw new InvalidException("Ph????ng ti???n ???? t???n t???i");
        }

        PhuongTien phuongTien = getPhuongTien(id);
        phuongTien.setSoThuTu(dto.getSoThuTu());
        phuongTien.setTenXe(dto.getTenXe());
        phuongTien.setBienSo(dto.getBienSo());
        phuongTien.setMauXe(dto.getMauXe());
        phuongTien.setSoChoNgoi(dto.getSoChoNgoi());
        phuongTienRepository.save(phuongTien);
        return phuongTien;
    }

    @Override
    public PhuongTien changeStatus(String id) {
        PhuongTien phuongTien = getPhuongTien(id);
        phuongTien.setTrangThai(!phuongTien.isTrangThai());
        phuongTienRepository.save(phuongTien);
        return phuongTien;
    }
}
