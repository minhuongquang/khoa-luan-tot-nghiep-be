package com.stc.thamquan.services.cauhoikhaosatcongtacvien;

import com.stc.thamquan.dtos.cauhoikhaosatcongtacvien.CauHoiKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.CauHoiKhaoSatCongTacVien;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.CauHoiKhaoSatCongtacVienRepository;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.utils.EnumLoaiCauHoi;
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
 * Filename  : CauHoiKhaoSatCongTacVienServiceImpl
 */
@Slf4j
@Service
public class CauHoiKhaoSatCongTacVienServiceImpl implements CauHoiKhaoSatCongTacVienService {
    private final CauHoiKhaoSatCongtacVienRepository cauHoiKhaoSatCongtacVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    public CauHoiKhaoSatCongTacVienServiceImpl(CauHoiKhaoSatCongtacVienRepository cauHoiKhaoSatCongtacVienRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService) {
        this.cauHoiKhaoSatCongtacVienRepository = cauHoiKhaoSatCongtacVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
    }


    @Override
    public Page<CauHoiKhaoSatCongTacVien> getAllCauHoiKhaoSatCongTacViensPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return cauHoiKhaoSatCongtacVienRepository.getAllCauHoiKhaoSatCongTacViensPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);

    }

    @Override
    public List<CauHoiKhaoSatCongTacVien> getAllCauHoiKhaoSatCongTacViensActive(String search, String idChuyenThamQuan) {
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(idChuyenThamQuan);
        if (chuyenThamQuan.getThoiHanLamKhaoSat().before(new LocalDateTime().toDate())) {
            throw new InvalidException("Hết thời hạn làm khảo sát");
        }
        return cauHoiKhaoSatCongtacVienRepository.getAllCauHoiKhaoSatCongTacViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public CauHoiKhaoSatCongTacVien getCauHoiKhaoSatCongTacVien(String id) {
        return cauHoiKhaoSatCongtacVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Câu hỏi khảo sát cộng tác viên có id %s không tồn tại", id)));
    }

    @Override
    public CauHoiKhaoSatCongTacVien createCauHoiKhaoSatCongTacVien(CauHoiKhaoSatCongTacVienDto dto) {
        CauHoiKhaoSatCongTacVien cauHoiKhaoSatCongTacVien = new CauHoiKhaoSatCongTacVien();
        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if (dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(1);
            } else if (dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())) {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát cộng tác viên không được bỏ trống");
            }
            cauHoiKhaoSatCongTacVien.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }

        cauHoiKhaoSatCongTacVien.setThuTu(dto.getThuTu());
        cauHoiKhaoSatCongTacVien.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatCongTacVien.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatCongTacVien.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatCongtacVienRepository.save(cauHoiKhaoSatCongTacVien);
        return cauHoiKhaoSatCongTacVien;
    }

    @Override
    public CauHoiKhaoSatCongTacVien updateCauHoiKhaoSatCongTacVien(String id, CauHoiKhaoSatCongTacVienDto dto) {
        CauHoiKhaoSatCongTacVien cauHoiKhaoSatCongTacVien = getCauHoiKhaoSatCongTacVien(id);
        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát cộng tác viên không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if (dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(1);
            } else if (dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())) {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else {
                cauHoiKhaoSatCongTacVien.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát cộng tác viên không được bỏ trống");
            }
            cauHoiKhaoSatCongTacVien.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }
        cauHoiKhaoSatCongTacVien.setThuTu(dto.getThuTu());
        cauHoiKhaoSatCongTacVien.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatCongTacVien.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatCongTacVien.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatCongtacVienRepository.save(cauHoiKhaoSatCongTacVien);
        return cauHoiKhaoSatCongTacVien;
    }

    @Override
    public CauHoiKhaoSatCongTacVien changeStatus(String id) {
        CauHoiKhaoSatCongTacVien cauHoiKhaoSatCongTacVien = getCauHoiKhaoSatCongTacVien(id);
        cauHoiKhaoSatCongTacVien.setTrangThai(!cauHoiKhaoSatCongTacVien.isTrangThai());
        cauHoiKhaoSatCongtacVienRepository.save(cauHoiKhaoSatCongTacVien);
        return cauHoiKhaoSatCongTacVien;
    }
}
