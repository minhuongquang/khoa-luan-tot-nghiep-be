package com.stc.thamquan.services.cauhoikhaosatdoanhnghiep;

import com.stc.thamquan.dtos.cauhoikhaosatdoanhnghiep.CauHoiKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.utils.EnumLoaiCauHoi;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.CauHoiKhaoSatDoanhNghiepRepository;
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
 * Filename  : CauHoiKhaoSatDoanhNghiepServiceImpl
 */
@Slf4j
@Service
public class CauHoiKhaoSatDoanhNghiepServiceImpl implements CauHoiKhaoSatDoanhNghiepService {

    private final CauHoiKhaoSatDoanhNghiepRepository cauHoiKhaoSatDoanhNghiepRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    public CauHoiKhaoSatDoanhNghiepServiceImpl(CauHoiKhaoSatDoanhNghiepRepository cauHoiKhaoSatDoanhNghiepRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService) {
        this.cauHoiKhaoSatDoanhNghiepRepository = cauHoiKhaoSatDoanhNghiepRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
    }

    @Override
    public Page<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatDoanhNghiepsPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return cauHoiKhaoSatDoanhNghiepRepository.getAllCauHoiKhaoSatsPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatDoanhNghiepsActive(String search, String idChuyenThamQuan) {
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(idChuyenThamQuan);
        if(chuyenThamQuan.getThoiHanLamKhaoSat().before(new LocalDateTime().toDate())){
            throw new InvalidException("Hết thời hạn làm khảo sát");
        }
        return cauHoiKhaoSatDoanhNghiepRepository.getAllCauHoiKhaoSatDoanhNghiepsActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public CauHoiKhaoSatDoanhNghiep getCauHoiKhaoSatDoanhNghiep(String id) {
        return cauHoiKhaoSatDoanhNghiepRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Câu hỏi khảo sát doanh nghiệp có id %s không tồn tại", id)));
    }

    @Override
    public CauHoiKhaoSatDoanhNghiep createCauHoiKhaoSatDoanhNghiep(CauHoiKhaoSatDoanhNghiepDto dto) {
        CauHoiKhaoSatDoanhNghiep cauHoiKhaoSatDoanhNghiep = new CauHoiKhaoSatDoanhNghiep();
        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if(ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(1);
            } else if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())){
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else{
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát doanh nghiệp không được bỏ trống");
            }
            cauHoiKhaoSatDoanhNghiep.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }

        cauHoiKhaoSatDoanhNghiep.setThuTu(dto.getThuTu());
        cauHoiKhaoSatDoanhNghiep.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatDoanhNghiep.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatDoanhNghiep.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatDoanhNghiepRepository.save(cauHoiKhaoSatDoanhNghiep);
        return cauHoiKhaoSatDoanhNghiep;
    }

    @Override
    public CauHoiKhaoSatDoanhNghiep updateCauHoiKhaoSatDoanhNghiep(String id, CauHoiKhaoSatDoanhNghiepDto dto) {
        CauHoiKhaoSatDoanhNghiep cauHoiKhaoSatDoanhNghiep = getCauHoiKhaoSatDoanhNghiep(id);
        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getCauHoi())) {
            throw new InvalidException("Câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getLoaiCauHoi())) {
            throw new InvalidException("Loại câu hỏi khảo sát doanh nghiệp không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.isCauHoiBatBuoc())) {
            throw new InvalidException("Câu hỏi bắt buộc không được bỏ trống");
        }
        if (!dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.NHAP_LIEU.name())) {
            if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_MOT.name())) {
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(1);
            } else if(dto.getLoaiCauHoi().equals(EnumLoaiCauHoi.CHON_NHIEU.name())){
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(dto.getLuaChonToiDa());
            } else{
                cauHoiKhaoSatDoanhNghiep.setLuaChonToiDa(dto.getDanhSachLuaChon().size());
            }
            if (ObjectUtils.isEmpty(dto.getDanhSachLuaChon())) {
                throw new InvalidException("Danh sách lựa chọn câu hỏi khảo sát doanh nghiệp không được bỏ trống");
            }
            cauHoiKhaoSatDoanhNghiep.setDanhSachLuaChon(dto.getDanhSachLuaChon());
        }
        cauHoiKhaoSatDoanhNghiep.setThuTu(dto.getThuTu());
        cauHoiKhaoSatDoanhNghiep.setCauHoi(dto.getCauHoi());
        cauHoiKhaoSatDoanhNghiep.setLoaiCauHoi(dto.getLoaiCauHoi());
        cauHoiKhaoSatDoanhNghiep.setCauHoiBatBuoc(dto.isCauHoiBatBuoc());
        cauHoiKhaoSatDoanhNghiepRepository.save(cauHoiKhaoSatDoanhNghiep);
        return cauHoiKhaoSatDoanhNghiep;
    }

    @Override
    public CauHoiKhaoSatDoanhNghiep changeStatus(String id) {
        CauHoiKhaoSatDoanhNghiep cauHoiKhaoSatDoanhNghiep = getCauHoiKhaoSatDoanhNghiep(id);
        cauHoiKhaoSatDoanhNghiep.setTrangThai(!cauHoiKhaoSatDoanhNghiep.isTrangThai());
        cauHoiKhaoSatDoanhNghiepRepository.save(cauHoiKhaoSatDoanhNghiep);
        return cauHoiKhaoSatDoanhNghiep;
    }
}
