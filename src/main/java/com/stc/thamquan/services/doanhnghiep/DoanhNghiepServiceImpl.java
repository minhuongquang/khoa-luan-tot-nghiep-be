package com.stc.thamquan.services.doanhnghiep;

import com.stc.thamquan.dtos.doanhnghiep.DoanhNghiepDto;
import com.stc.thamquan.entities.DoanhNghiep;
import com.stc.thamquan.entities.LinhVuc;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.DoanhNghiepRepository;
import com.stc.thamquan.repositories.LinhVucRepository;
import com.stc.thamquan.services.excel.ExcelService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 4/14/21
 * Time      : 16:08
 * Filename  : DoanhNghiepServiceImpl
 */
@Slf4j
@Service
public class DoanhNghiepServiceImpl implements DoanhNghiepService {

    private final DoanhNghiepRepository doanhNghiepRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ExcelService excelService;

    private final LinhVucRepository linhVucRepository;

    public DoanhNghiepServiceImpl(DoanhNghiepRepository doanhNghiepRepository, VietnameseStringUtils vietnameseStringUtils, ExcelService excelService, LinhVucRepository linhVucRepository) {
        this.doanhNghiepRepository = doanhNghiepRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.excelService = excelService;
        this.linhVucRepository = linhVucRepository;
    }


    @Override
    public Page<DoanhNghiep> getAllDoanhNghiepsPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return doanhNghiepRepository.getAllDoanhNghiepsPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<DoanhNghiep> getAllDoanhNghiepsActive(String search) {
        return doanhNghiepRepository.getAllDoanhNghiepsActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public DoanhNghiep getDoanhNghiep(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return doanhNghiepRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Doanh nghi???p c?? id %s kh??ng t???n t???i", id)));
    }

    @Override
    public DoanhNghiep getDoanhNghiepByIdCore(String id) {
        return doanhNghiepRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public DoanhNghiep createDoanhNghiep(DoanhNghiepDto dto) {
        DoanhNghiep doanhNghiep = new DoanhNghiep();

        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new InvalidException("Email doanh nghi???p kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("H??? t??n doanh nghi???p kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.isGioiTinh())) {
            throw new InvalidException("Gi???i t??nh kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getDienThoai())) {
            throw new InvalidException("??i???n tho???i kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getCongTy())) {
            throw new InvalidException("C??ng ty kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getDiaChi())) {
            throw new InvalidException("?????a ch??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getMaSoThue())) {
            throw new InvalidException("M?? s??? thu??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getLinhVucs())) {
            throw new InvalidException("L??nh v???c kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getRoles())) {
            throw new InvalidException("Quy???n kh??ng ???????c b??? tr???ng");
        }

        if (doanhNghiepRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new InvalidException("Email ???? t???n t???i.");
        }
        if (doanhNghiepRepository.existsByMaSoThueIgnoreCase(dto.getMaSoThue())) {
            throw new InvalidException("M?? s??? thu??? ???? t???n t???i");
        }

        //TODO: x??? l?? l??nh v???c t??? string sang array

        List<String> linhVucs = dto.getLinhVucs();


        List<LinhVuc> linhVucList = new ArrayList<>();
        for (int i = 0; i < linhVucs.size(); i++) {
            LinhVuc lv = linhVucRepository.findById(linhVucs.get(i)).orElse(null);
            linhVucList.add(lv);
        }


        doanhNghiep.setEmail(dto.getEmail().trim().toLowerCase());
        doanhNghiep.setHoTen(dto.getHoTen());
        doanhNghiep.setGioiTinh(dto.isGioiTinh());
        doanhNghiep.setDienThoai(dto.getDienThoai());
        doanhNghiep.setCongTy(dto.getCongTy());
        doanhNghiep.setDiaChi(dto.getDiaChi());
        doanhNghiep.setMaSoThue(dto.getMaSoThue());
        doanhNghiep.setLinhVucs(linhVucList);
        doanhNghiep.setRoles(dto.getRoles());
        doanhNghiepRepository.save(doanhNghiep);
        return doanhNghiep;
    }

    @Override
    public DoanhNghiep updateDoanhNghiep(String id, DoanhNghiepDto dto) {

        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("H??? t??n doanh nghi???p kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.isGioiTinh())) {
            throw new InvalidException("Gi???i t??nh kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getDienThoai())) {
            throw new InvalidException("??i???n tho???i kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getCongTy())) {
            throw new InvalidException("C??ng ty kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getDiaChi())) {
            throw new InvalidException("?????a ch??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getMaSoThue())) {
            throw new InvalidException("M?? s??? thu??? kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getLinhVucs())) {
            throw new InvalidException("L??nh v???c kh??ng ???????c b??? tr???ng");
        }

        if (ObjectUtils.isEmpty(dto.getRoles())) {
            throw new InvalidException("Quy???n kh??ng ???????c b??? tr???ng");
        }

        //TODO: x??? l?? l??nh v???c t??? string sang array

        List<String> linhVucs = dto.getLinhVucs();


        List<LinhVuc> linhVucList = new ArrayList<>();
        for (int i = 0; i < linhVucs.size(); i++) {
            LinhVuc lv = linhVucRepository.findById(linhVucs.get(i)).orElse(null);
            linhVucList.add(lv);
        }

        DoanhNghiep doanhNghiep = getDoanhNghiep(id);
        doanhNghiep.setHoTen(dto.getHoTen());
        doanhNghiep.setGioiTinh(dto.isGioiTinh());
        doanhNghiep.setDienThoai(dto.getDienThoai());
        doanhNghiep.setCongTy(dto.getCongTy());
        doanhNghiep.setDiaChi(dto.getDiaChi());
        doanhNghiep.setMaSoThue(dto.getMaSoThue());
        doanhNghiep.setLinhVucs(linhVucList);
        doanhNghiep.setRoles(dto.getRoles());
        doanhNghiepRepository.save(doanhNghiep);
        return doanhNghiep;
    }

    @Override
    public DoanhNghiep changeStatus(String id) {
        DoanhNghiep doanhNghiep = getDoanhNghiep(id);
        doanhNghiep.setTrangThai(!doanhNghiep.isTrangThai());
        doanhNghiepRepository.save(doanhNghiep);
        return doanhNghiep;
    }

    @Override
    public void importDanhSachDoanhNghieps(MultipartFile file) throws Exception {
        excelService.importDanhSachDoanhNghieps(file);
    }

    @Override
    public DoanhNghiep getCurrentDoanhNghiep(Principal principal) {
        return doanhNghiepRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("T??i kho???n c?? email %s kh??ng t???n t???i", principal.getName())));
    }
}
