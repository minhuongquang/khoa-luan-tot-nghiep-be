package com.stc.thamquan.services.ketquakhaosatdoanhnghiep;

import com.stc.thamquan.dtos.ketquakhaosatdoanhnghiep.KetQuaKhaoSatDoanhNghiepDto;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.KetQuaKhaoSat;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.KetQuaKhaoSatDoanhNghiepRepository;
import com.stc.thamquan.services.cauhoikhaosatdoanhnghiep.CauHoiKhaoSatDoanhNghiepService;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.services.doanhnghiep.DoanhNghiepService;
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
 * Filename  : KetQuaKhaoSatDoanhNghiepServiceImpl
 */
@Slf4j
@Service
public class KetQuaKhaoSatDoanhNghiepServiceImpl implements KetQuaKhaoSatDoanhNghiepService {
    private final KetQuaKhaoSatDoanhNghiepRepository ketQuaKhaoSatDoanhNghiepRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    private final DoanhNghiepService doanhNghiepService;

    private final CauHoiKhaoSatDoanhNghiepService cauHoiKhaoSatDoanhNghiepService;

    public KetQuaKhaoSatDoanhNghiepServiceImpl(KetQuaKhaoSatDoanhNghiepRepository ketQuaKhaoSatDoanhNghiepRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService, DoanhNghiepService doanhNghiepService, CauHoiKhaoSatDoanhNghiepService cauHoiKhaoSatDoanhNghiepService) {
        this.ketQuaKhaoSatDoanhNghiepRepository = ketQuaKhaoSatDoanhNghiepRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
        this.doanhNghiepService = doanhNghiepService;
        this.cauHoiKhaoSatDoanhNghiepService = cauHoiKhaoSatDoanhNghiepService;
    }

    @Override
    public Page<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return ketQuaKhaoSatDoanhNghiepRepository.getAllKetQuaKhaoSatDoanhNghiepsPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsActive(String search) {
        return ketQuaKhaoSatDoanhNghiepRepository.getAllKetQuaKhaoSatDoanhNghiepsActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public KetQuaKhaoSatDoanhNghiep getKetQuaKhaoSatDoanhNghiep(String id) {
        return ketQuaKhaoSatDoanhNghiepRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Kết quả khảo sát doanh nghiệp có id %s không tồn tại", id)));
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiep> createKetQuaKhaoSatDoanhNghiep(KetQuaKhaoSatDoanhNghiepDto dto, Principal principal) {
        if (ObjectUtils.isEmpty(dto.getChuyenThamQuan())) {
            throw new InvalidException("Chuyến tham quan không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKetQuaKhaoSats())) {
            throw new InvalidException("Kết quả khảo sát không được bỏ trống.");
        }
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(dto.getChuyenThamQuan());
        DoanhNghiep doanhNghiep = doanhNghiepService.getCurrentDoanhNghiep(principal);
        List<KetQuaKhaoSatDoanhNghiep> ketQuaKhaoSatDoanhNghiepList = ketQuaKhaoSatDoanhNghiepRepository.findKetQuaKhaoSatDoanhNghiepsByUserKhaoSatAndChuyenThamQuan(doanhNghiep, chuyenThamQuan);
        if (ketQuaKhaoSatDoanhNghiepList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }
        List<KetQuaKhaoSatDoanhNghiep> ketQuaKhaoSatDoanhNghieps = new ArrayList<>();
        for (KetQuaKhaoSat ketQuaKhaoSat : dto.getKetQuaKhaoSats()) {
            KetQuaKhaoSatDoanhNghiep ketQuaKhaoSatDoanhNghiep = new KetQuaKhaoSatDoanhNghiep();
            ketQuaKhaoSatDoanhNghiep.setChuyenThamQuan(chuyenThamQuan);
            ketQuaKhaoSatDoanhNghiep.setUserKhaoSat(doanhNghiep);
            ketQuaKhaoSatDoanhNghiep.setThoiGianTraLoi(new LocalDateTime().toDate());
            CauHoiKhaoSatDoanhNghiep cauHoiKhaoSatDoanhNghiep = cauHoiKhaoSatDoanhNghiepService.getCauHoiKhaoSatDoanhNghiep(ketQuaKhaoSat.getCauHoiKhaoSat());
            ketQuaKhaoSatDoanhNghiep.setCauHoi(cauHoiKhaoSatDoanhNghiep);
            ketQuaKhaoSatDoanhNghiep.setCauTraLoi(ketQuaKhaoSat.getCauTraLoi());
            ketQuaKhaoSatDoanhNghieps.add(ketQuaKhaoSatDoanhNghiep);
        }
        ketQuaKhaoSatDoanhNghieps.stream().map(kqksdn -> ketQuaKhaoSatDoanhNghiepRepository.save(kqksdn)).collect(Collectors.toList());

        return ketQuaKhaoSatDoanhNghieps;
    }

    @Override
    public KetQuaKhaoSatDoanhNghiep updateKetQuaKhaoSatDoanhNghiep(String id, KetQuaKhaoSatDoanhNghiepDto dto, Principal principal) {
        KetQuaKhaoSatDoanhNghiep ketQuaKhaoSatDoanhNghiep = getKetQuaKhaoSatDoanhNghiep(id);

        if (ObjectUtils.isEmpty(dto.getChuyenThamQuan())) {
            throw new InvalidException("Chuyến tham quan không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKetQuaKhaoSats())) {
            throw new InvalidException("Kết quả khảo sát không được bỏ trống.");
        }
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(dto.getChuyenThamQuan());
        DoanhNghiep doanhNghiep = doanhNghiepService.getCurrentDoanhNghiep(principal);
        for (KetQuaKhaoSat ketQuaKhaoSat : dto.getKetQuaKhaoSats()) {
            ketQuaKhaoSatDoanhNghiep.setChuyenThamQuan(chuyenThamQuan);
            ketQuaKhaoSatDoanhNghiep.setUserKhaoSat(doanhNghiep);
            ketQuaKhaoSatDoanhNghiep.setThoiGianTraLoi(new LocalDateTime().toDate());
            CauHoiKhaoSatDoanhNghiep cauHoiKhaoSatDoanhNghiep = cauHoiKhaoSatDoanhNghiepService.getCauHoiKhaoSatDoanhNghiep(ketQuaKhaoSat.getCauHoiKhaoSat());
            ketQuaKhaoSatDoanhNghiep.setCauHoi(cauHoiKhaoSatDoanhNghiep);
            ketQuaKhaoSatDoanhNghiep.setCauTraLoi(ketQuaKhaoSat.getCauTraLoi());
            ketQuaKhaoSatDoanhNghiepRepository.save(ketQuaKhaoSatDoanhNghiep);
        }
        return ketQuaKhaoSatDoanhNghiep;
    }

    @Override
    public KetQuaKhaoSatDoanhNghiep changeStatus(String id) {
        KetQuaKhaoSatDoanhNghiep ketQuaKhaoSatDoanhNghiep = getKetQuaKhaoSatDoanhNghiep(id);
        ketQuaKhaoSatDoanhNghiep.setTrangThai(!ketQuaKhaoSatDoanhNghiep.isTrangThai());
        ketQuaKhaoSatDoanhNghiepRepository.save(ketQuaKhaoSatDoanhNghiep);
        return ketQuaKhaoSatDoanhNghiep;
    }

    @Override
    public boolean checkKhaoSat(String chuyenThamQuanId, Principal principal) {
        DoanhNghiep doanhNghiep = doanhNghiepService.getCurrentDoanhNghiep(principal);
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(chuyenThamQuanId);
        List<KetQuaKhaoSatDoanhNghiep> ketQuaKhaoSatDoanhNghiepList = ketQuaKhaoSatDoanhNghiepRepository.findKetQuaKhaoSatDoanhNghiepsByUserKhaoSatAndChuyenThamQuan(doanhNghiep, chuyenThamQuan);

        if (ketQuaKhaoSatDoanhNghiepList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }
        return false;
    }
}
