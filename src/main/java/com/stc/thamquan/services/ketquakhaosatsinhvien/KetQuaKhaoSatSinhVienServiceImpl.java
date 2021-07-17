package com.stc.thamquan.services.ketquakhaosatsinhvien;

import com.stc.thamquan.dtos.ketquakhaosatsinhvien.KetQuaKhaoSatSinhVienDto;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.KetQuaKhaoSat;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.KetQuaKhaoSatSinhVienRepository;
import com.stc.thamquan.services.cauhoikhaosatsinhvien.CauHoiKhaoSatSinhVienService;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.services.sinhvien.SinhVienService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 16:08
 * Filename  : KetQuaKhaoSatSinhVienServiceImpl
 */
@Slf4j
@Service
public class KetQuaKhaoSatSinhVienServiceImpl implements KetQuaKhaoSatSinhVienService {
    private final KetQuaKhaoSatSinhVienRepository ketQuaKhaoSatSinhVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    private final CauHoiKhaoSatSinhVienService cauHoiKhaoSatSinhVienService;

    private final SinhVienService sinhVienService;


    public KetQuaKhaoSatSinhVienServiceImpl(KetQuaKhaoSatSinhVienRepository ketQuaKhaoSatSinhVienRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService, CauHoiKhaoSatSinhVienService cauHoiKhaoSatSinhVienService, SinhVienService sinhVienService) {
        this.ketQuaKhaoSatSinhVienRepository = ketQuaKhaoSatSinhVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
        this.cauHoiKhaoSatSinhVienService = cauHoiKhaoSatSinhVienService;
        this.sinhVienService = sinhVienService;
    }

    @Override
    public Page<KetQuaKhaoSatSinhVien> getAllKetQuaKhaoSatSinhViensPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return ketQuaKhaoSatSinhVienRepository.getAllKetQuaKhaoSatSinhViensPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<KetQuaKhaoSatSinhVien> getAllKetQuaKhaoSatSinhViensActive(String search) {
        return ketQuaKhaoSatSinhVienRepository.getAllKetQuaKhaoSatSinhViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public KetQuaKhaoSatSinhVien getKetQuaKhaoSatSinhVien(String id) {
        return ketQuaKhaoSatSinhVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Kết quả khảo sát doanh nghiệp có id %s không tồn tại", id)));
    }

    @Override
    public List<KetQuaKhaoSatSinhVien> createKetQuaKhaoSatSinhVien(KetQuaKhaoSatSinhVienDto dto, Principal principal) {
        if (ObjectUtils.isEmpty(dto.getChuyenThamQuan())) {
            throw new InvalidException("Chuyến tham quan không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKetQuaKhaoSats())) {
            throw new InvalidException("Kết quả khảo sát không được bỏ trống.");
        }
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(dto.getChuyenThamQuan());
        SinhVien sinhVien = sinhVienService.getCurrentSinhVien(principal);

        List<KetQuaKhaoSatSinhVien> ketQuaKhaoSatSinhVienList = ketQuaKhaoSatSinhVienRepository.findKetQuaKhaoSatSinhViensByUserKhaoSatAndChuyenThamQuan(sinhVien, chuyenThamQuan);

        if (ketQuaKhaoSatSinhVienList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }

        List<KetQuaKhaoSatSinhVien> ketQuaKhaoSatSinhViens = new ArrayList<>();
        for (KetQuaKhaoSat ketQuaKhaoSat : dto.getKetQuaKhaoSats()) {
            KetQuaKhaoSatSinhVien ketQuaKhaoSatSinhVien = new KetQuaKhaoSatSinhVien();
            CauHoiKhaoSatSinhVien cauHoiKhaoSatSinhVien = cauHoiKhaoSatSinhVienService.getCauHoiKhaoSatSinhVien(ketQuaKhaoSat.getCauHoiKhaoSat());
            ketQuaKhaoSatSinhVien.setChuyenThamQuan(chuyenThamQuan);
            ketQuaKhaoSatSinhVien.setUserKhaoSat(sinhVien);
            ketQuaKhaoSatSinhVien.setThoiGianTraLoi(new LocalDateTime().toDate());
            ketQuaKhaoSatSinhVien.setCauHoi(cauHoiKhaoSatSinhVien);
            ketQuaKhaoSatSinhVien.setCauTraLoi(ketQuaKhaoSat.getCauTraLoi());
            ketQuaKhaoSatSinhViens.add(ketQuaKhaoSatSinhVien);
        }
        ketQuaKhaoSatSinhViens.stream().map(kqksdn -> ketQuaKhaoSatSinhVienRepository.save(kqksdn)).collect(Collectors.toList());

        return ketQuaKhaoSatSinhViens;
    }

    @Override
    public KetQuaKhaoSatSinhVien changeStatus(String id) {
        KetQuaKhaoSatSinhVien ketQuaKhaoSatSinhVien = getKetQuaKhaoSatSinhVien(id);
        ketQuaKhaoSatSinhVien.setTrangThai(!ketQuaKhaoSatSinhVien.isTrangThai());
        ketQuaKhaoSatSinhVienRepository.save(ketQuaKhaoSatSinhVien);
        return ketQuaKhaoSatSinhVien;
    }

    @Override
    public boolean checkKhaoSat(String chuyenThamQuanId, Principal principal) {
        SinhVien sinhVien = sinhVienService.getCurrentSinhVien(principal);
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(chuyenThamQuanId);
        List<KetQuaKhaoSatSinhVien> ketQuaKhaoSatSinhVienList = ketQuaKhaoSatSinhVienRepository.findKetQuaKhaoSatSinhViensByUserKhaoSatAndChuyenThamQuan(sinhVien, chuyenThamQuan);

        if (ketQuaKhaoSatSinhVienList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }
        return false;
    }
}
