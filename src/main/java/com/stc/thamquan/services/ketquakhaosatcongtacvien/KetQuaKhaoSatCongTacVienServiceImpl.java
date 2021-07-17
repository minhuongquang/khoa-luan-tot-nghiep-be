package com.stc.thamquan.services.ketquakhaosatcongtacvien;

import com.stc.thamquan.dtos.ketquakhaosatcongtacvien.KetQuaKhaoSatCongTacVienDto;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.KetQuaKhaoSat;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.KetQuaKhaoSatCongTacVienRepository;
import com.stc.thamquan.services.cauhoikhaosatcongtacvien.CauHoiKhaoSatCongTacVienService;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.services.congtacvien.CongTacVienService;
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
 * Filename  : KetQuaKhaoSatCongTacVienServiceImpl
 */
@Slf4j
@Service
public class KetQuaKhaoSatCongTacVienServiceImpl implements KetQuaKhaoSatCongTacVienService {
    private final KetQuaKhaoSatCongTacVienRepository ketQuaKhaoSatCongTacVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ChuyenThamQuanService chuyenThamQuanService;

    private final CongTacVienService congTacVienService;

    private final CauHoiKhaoSatCongTacVienService cauHoiKhaoSatCongTacVienService;

    public KetQuaKhaoSatCongTacVienServiceImpl(KetQuaKhaoSatCongTacVienRepository ketQuaKhaoSatCongTacVienRepository, VietnameseStringUtils vietnameseStringUtils, ChuyenThamQuanService chuyenThamQuanService, CongTacVienService congTacVienService, CauHoiKhaoSatCongTacVienService cauHoiKhaoSatCongTacVienService) {
        this.ketQuaKhaoSatCongTacVienRepository = ketQuaKhaoSatCongTacVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.chuyenThamQuanService = chuyenThamQuanService;
        this.congTacVienService = congTacVienService;
        this.cauHoiKhaoSatCongTacVienService = cauHoiKhaoSatCongTacVienService;
    }


    @Override
    public Page<KetQuaKhaoSatCongTacVien> getAllKetQuaKhaoSatCongTacViensPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return ketQuaKhaoSatCongTacVienRepository.getAllKetQuaKhaoSatCongTacViensPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<KetQuaKhaoSatCongTacVien> getAllKetQuaKhaoSatCongTacViensActive(String search) {
        return ketQuaKhaoSatCongTacVienRepository.getAllKetQuaKhaoSatCongTacViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public KetQuaKhaoSatCongTacVien getKetQuaKhaoSatCongTacVien(String id) {
        return ketQuaKhaoSatCongTacVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Kết quả khảo sát cộng tác viên có id %s không tồn tại", id)));
    }

    @Override
    public List<KetQuaKhaoSatCongTacVien> createKetQuaKhaoSatCongTacVien(KetQuaKhaoSatCongTacVienDto dto, Principal principal) {
        if (ObjectUtils.isEmpty(dto.getChuyenThamQuan())) {
            throw new InvalidException("Chuyến tham quan không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKetQuaKhaoSats())) {
            throw new InvalidException("Kết quả khảo sát không được bỏ trống.");
        }
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(dto.getChuyenThamQuan());
        CongTacVien congTacVien = congTacVienService.getCurrent(principal);

        List<KetQuaKhaoSatCongTacVien> ketQuaKhaoSatCongTacVienList = ketQuaKhaoSatCongTacVienRepository.findKetQuaKhaoSatCongTacViensByUserKhaoSatAndChuyenThamQuan(congTacVien, chuyenThamQuan);
        if (ketQuaKhaoSatCongTacVienList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }
        List<KetQuaKhaoSatCongTacVien> ketQuaKhaoSatCongTacViens = new ArrayList<>();
        for (KetQuaKhaoSat ketQuaKhaoSat : dto.getKetQuaKhaoSats()) {
            KetQuaKhaoSatCongTacVien ketQuaKhaoSatCongTacVien = new KetQuaKhaoSatCongTacVien();
            ketQuaKhaoSatCongTacVien.setChuyenThamQuan(chuyenThamQuan);
            ketQuaKhaoSatCongTacVien.setUserKhaoSat(congTacVien);
            ketQuaKhaoSatCongTacVien.setThoiGianTraLoi(new LocalDateTime().toDate());
            CauHoiKhaoSatCongTacVien cauHoiKhaoSatCongTacVien = cauHoiKhaoSatCongTacVienService.getCauHoiKhaoSatCongTacVien(ketQuaKhaoSat.getCauHoiKhaoSat());
            ketQuaKhaoSatCongTacVien.setCauHoi(cauHoiKhaoSatCongTacVien);
            ketQuaKhaoSatCongTacVien.setCauTraLoi(ketQuaKhaoSat.getCauTraLoi());
            ketQuaKhaoSatCongTacViens.add(ketQuaKhaoSatCongTacVien);
        }
        ketQuaKhaoSatCongTacViens.stream().map(kqksdn -> ketQuaKhaoSatCongTacVienRepository.save(kqksdn)).collect(Collectors.toList());

        return ketQuaKhaoSatCongTacViens;
    }

    @Override
    public KetQuaKhaoSatCongTacVien updateKetQuaKhaoSatCongTacVien(String id, KetQuaKhaoSatCongTacVienDto dto, Principal principal) {
        KetQuaKhaoSatCongTacVien ketQuaKhaoSatCongTacVien = getKetQuaKhaoSatCongTacVien(id);

        if (ObjectUtils.isEmpty(dto.getChuyenThamQuan())) {
            throw new InvalidException("Chuyến tham quan không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKetQuaKhaoSats())) {
            throw new InvalidException("Kết quả khảo sát không được bỏ trống.");
        }
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(dto.getChuyenThamQuan());
        CongTacVien CongTacVien = congTacVienService.getCurrent(principal);
        for (KetQuaKhaoSat ketQuaKhaoSat : dto.getKetQuaKhaoSats()) {
            ketQuaKhaoSatCongTacVien.setChuyenThamQuan(chuyenThamQuan);
            ketQuaKhaoSatCongTacVien.setUserKhaoSat(CongTacVien);
            ketQuaKhaoSatCongTacVien.setThoiGianTraLoi(new LocalDateTime().toDate());
            CauHoiKhaoSatCongTacVien cauHoiKhaoSatCongTacVien = cauHoiKhaoSatCongTacVienService.getCauHoiKhaoSatCongTacVien(ketQuaKhaoSat.getCauHoiKhaoSat());
            ketQuaKhaoSatCongTacVien.setCauHoi(cauHoiKhaoSatCongTacVien);
            ketQuaKhaoSatCongTacVien.setCauTraLoi(ketQuaKhaoSat.getCauTraLoi());
            ketQuaKhaoSatCongTacVienRepository.save(ketQuaKhaoSatCongTacVien);
        }
        return ketQuaKhaoSatCongTacVien;
    }

    @Override
    public KetQuaKhaoSatCongTacVien changeStatus(String id) {
        KetQuaKhaoSatCongTacVien ketQuaKhaoSatCongTacVien = getKetQuaKhaoSatCongTacVien(id);
        ketQuaKhaoSatCongTacVien.setTrangThai(!ketQuaKhaoSatCongTacVien.isTrangThai());
        ketQuaKhaoSatCongTacVienRepository.save(ketQuaKhaoSatCongTacVien);
        return ketQuaKhaoSatCongTacVien;
    }
    @Override
    public boolean checkKhaoSat(String chuyenThamQuanId, Principal principal) {
        CongTacVien congTacVien = congTacVienService.getCurrent(principal);
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanService.getChuyenThamQuan(chuyenThamQuanId);
        List<KetQuaKhaoSatCongTacVien> ketQuaKhaoSatCongTacVienList = ketQuaKhaoSatCongTacVienRepository.findKetQuaKhaoSatCongTacViensByUserKhaoSatAndChuyenThamQuan(congTacVien, chuyenThamQuan);

        if (ketQuaKhaoSatCongTacVienList.size() > 0) {
            throw new InvalidException("Bạn đã thực hiện khảo sát.");
        }
        return false;
    }
}
