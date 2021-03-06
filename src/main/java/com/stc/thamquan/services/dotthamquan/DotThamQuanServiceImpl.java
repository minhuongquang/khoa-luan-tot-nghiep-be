package com.stc.thamquan.services.dotthamquan;

import com.stc.thamquan.dtos.dotthamquan.DotThamQuanDto;
import com.stc.thamquan.entities.DotThamQuan;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.DotThamQuanRepository;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:59
 * Filename  : DotThamQuanServiceImpl
 */
@Slf4j
@Service
public class DotThamQuanServiceImpl implements DotThamQuanService {
    private final DotThamQuanRepository dotThamQuanRepository;

    private final VietnameseStringUtils vietnameseStringUtils;


    public DotThamQuanServiceImpl(DotThamQuanRepository dotThamQuanRepository, VietnameseStringUtils vietnameseStringUtils) {
        this.dotThamQuanRepository = dotThamQuanRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }


    @Override
    public Page<DotThamQuan> getAllDotThamQuansPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return dotThamQuanRepository.getAllDotThamQuansPaging((vietnameseStringUtils.makeSearchRegex(search)), pageable);
    }

    @Override
    public Page<DotThamQuan> getAllDotThamQuanActive(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return dotThamQuanRepository.getAllDotThamQuanActive(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }


    @Override
    public DotThamQuan getDotThamQuan(String id) {
        return dotThamQuanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("?????t tham quan c?? id %s kh??ng t???n t???i", id)));
    }

    @Override
    public DotThamQuan getDotThamQuanByIdCore(String id) {
        return dotThamQuanRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public DotThamQuan createDotThamQuan(DotThamQuanDto dto) {
        DotThamQuan dotThamQuan = new DotThamQuan();
        if (ObjectUtils.isEmpty((dto.getTenDotThamQuan()))) {
            throw new InvalidException("T??n ?????t tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getMucDich()))) {
            throw new InvalidException("M???c ????ch tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getYeuCau()))) {
            throw new InvalidException("Y??u c???u tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getNoiDungThamQuan()))) {
            throw new InvalidException("N???i dung tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty(dto.getTuNgay())) {
            throw new InvalidException("Ng??y ??i kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty(dto.getDenNgay())) {
            throw new InvalidException("Ng??y v??? kh??ng ???????c b??? tr???ng.");
        }
        if (dto.getTuNgay().after(dto.getDenNgay())) {
            throw new InvalidException("Vui l??ng xem l???i th???i gian ??i v?? th???i gian v???.");
        }

        dotThamQuan.setTenDotThamQuan(dto.getTenDotThamQuan());
        dotThamQuan.setTuNgay(dto.getTuNgay());
        dotThamQuan.setDenNgay(dto.getDenNgay());
        dotThamQuan.setNamHoc(dto.getNamHoc());
        dotThamQuan.setHocKy(dto.getHocKy());
        dotThamQuan.setYeuCau(dto.getYeuCau());
        dotThamQuan.setNoiDungThamQuan(dto.getNoiDungThamQuan());
        dotThamQuan.setMucDich(dto.getMucDich());
        dotThamQuan.setTrangThai(true);
        dotThamQuanRepository.save(dotThamQuan);
        return dotThamQuan;
    }

    @Override
    public DotThamQuan updateDotThamQuan(String id, DotThamQuanDto dto) {
        if (ObjectUtils.isEmpty((dto.getTenDotThamQuan()))) {
            throw new InvalidException("T??n ?????t tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getMucDich()))) {
            throw new InvalidException("M???c ????ch tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getYeuCau()))) {
            throw new InvalidException("Y??u c???u tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty((dto.getNoiDungThamQuan()))) {
            throw new InvalidException("N???i dung tham quan kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty(dto.getTuNgay())) {
            throw new InvalidException("Ng??y ??i kh??ng ???????c b??? tr???ng.");
        }
        if (ObjectUtils.isEmpty(dto.getDenNgay())) {
            throw new InvalidException("Ng??y v??? kh??ng ???????c b??? tr???ng.");
        }
        if (dto.getTuNgay().after(dto.getDenNgay())) {
            throw new InvalidException("Vui l??ng xem l???i th???i gian ??i v?? th???i gian v???.");
        }
        DotThamQuan dotThamQuan = getDotThamQuan(id);
        dotThamQuan.setTenDotThamQuan(dto.getTenDotThamQuan());
        dotThamQuan.setTuNgay(dto.getTuNgay());
        dotThamQuan.setDenNgay(dto.getDenNgay());
        dotThamQuan.setNamHoc(dto.getNamHoc());
        dotThamQuan.setHocKy(dto.getHocKy());
        dotThamQuan.setYeuCau(dto.getYeuCau());
        dotThamQuan.setNoiDungThamQuan(dto.getNoiDungThamQuan());
        dotThamQuan.setMucDich(dto.getMucDich());
        dotThamQuanRepository.save(dotThamQuan);
        return dotThamQuan;
    }

    @Override
    public DotThamQuan changeStatus(String id) {
        DotThamQuan dotThamQuan = getDotThamQuan(id);
        dotThamQuan.setTrangThai(!dotThamQuan.isTrangThai());
        dotThamQuanRepository.save(dotThamQuan);
        return dotThamQuan;
    }

    @Override
    public List<DotThamQuan> getAllByTrangThaiTrue() {
        return dotThamQuanRepository.getAllByTrangThaiTrue();
    }


}
