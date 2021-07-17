package com.stc.thamquan.services.cauhoikhaosatsinhvien;


import com.stc.thamquan.dtos.cauhoikhaosatsinhvien.CauHoiKhaoSatSinhVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatSinhVien;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.utils.EnumLoaiCauHoi;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.CauHoiKhaoSatSinhVienRepository;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : CauHoiKhaoSatSinhVienServiceImpl
 */
@Slf4j
@Service
public class CauHoiKhaoSatSinhVienServiceImpl implements CauHoiKhaoSatSinhVienService {
    private final CauHoiKhaoSatSinhVienRepository cauHoiKhaoSatSinhVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    public CauHoiKhaoSatSinhVienServiceImpl(CauHoiKhaoSatSinhVienRepository cauHoiKhaoSatSinhVienRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService) {
        this.cauHoiKhaoSatSinhVienRepository = cauHoiKhaoSatSinhVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
    }

    @Override
    public Page<CauHoiKhaoSatSinhVien> getAllCauHoiKhaoSatSinhViensPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return cauHoiKhaoSatSinhVienRepository.getAllCauHoiKhaoSatSinhViensPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<CauHoiKhaoSatSinhVien> getAllCauHoiKhaoSatSinhViensActive(String search, String idChuyenThamQuan) {
        ChuyenThamQuan chuyenThamQuan= chuyenThamQuanService.getChuyenThamQuan(idChuyenThamQuan);
        if(chuyenThamQuan.getThoiHanLamKhaoSat().before(new LocalDateTime().toDate())){
            throw new InvalidException("Hết thời hạn làm khảo sát");
        }
        return cauHoiKhaoSatSinhVienRepository.getAllCauHoiKhaoSatSinhViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public CauHoiKhaoSatSinhVien getCauHoiKhaoSatSinhVien(String id) {
        return cauHoiKhaoSatSinhVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Câu hỏi khảo sát sinh viên có id %s không tồn tại", id)));
    }

    @Override
    public CauHoiKhaoSatSinhVien createCauHoiKhaoSatSinhVien(CauHoiKhaoSatSinhVienDto dto) {
        CauHoiKhaoSatSinhVien cauHoiKhaoSatSinhVien = new CauHoiKhaoSatSinhVien();

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(1);
            } else if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())){
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else{
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát sinh viên không được bỏ trống");
            }
            cauHoiKhaoSatSinhVien.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }
        cauHoiKhaoSatSinhVien.setThuTu(dto.getThuTu());
        cauHoiKhaoSatSinhVien.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatSinhVien.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatSinhVien.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatSinhVienRepository.save(cauHoiKhaoSatSinhVien);
        return cauHoiKhaoSatSinhVien;
    }

    @Override
    public CauHoiKhaoSatSinhVien updateCauHoiKhaoSatSinhVien(String id, CauHoiKhaoSatSinhVienDto dto) {
        CauHoiKhaoSatSinhVien cauHoiKhaoSatSinhVien = getCauHoiKhaoSatSinhVien(id);
        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát sinh viên không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(1);
            } else if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())){
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else{
                cauHoiKhaoSatSinhVien.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát sinh viên không được bỏ trống");
            }
            cauHoiKhaoSatSinhVien.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }
        cauHoiKhaoSatSinhVien.setThuTu(dto.getThuTu());
        cauHoiKhaoSatSinhVien.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatSinhVien.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatSinhVien.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatSinhVienRepository.save(cauHoiKhaoSatSinhVien);
        return cauHoiKhaoSatSinhVien;
    }

    @Override
    public CauHoiKhaoSatSinhVien changeStatus(String id) {
        CauHoiKhaoSatSinhVien cauHoiKhaoSatSinhVien = getCauHoiKhaoSatSinhVien(id);
        cauHoiKhaoSatSinhVien.setTrangThai(!cauHoiKhaoSatSinhVien.isTrangThai());
        cauHoiKhaoSatSinhVienRepository.save(cauHoiKhaoSatSinhVien);
        return cauHoiKhaoSatSinhVien;
    }
}
