package com.stc.thamquan.services.giangvien;

import com.stc.thamquan.dtos.giangvien.FilterGiangVienDto;
import com.stc.thamquan.dtos.giangvien.GiangVienDto;
import com.stc.thamquan.dtos.giangvien.PageGiangVienTransfer;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.GiangVienRepository;
import com.stc.thamquan.services.hocham.HocHamService;
import com.stc.thamquan.services.hocvi.HocViService;
import com.stc.thamquan.services.khoa.KhoaService;
import com.stc.thamquan.services.linhvuc.LinhVucService;
import com.stc.thamquan.services.nganh.NganhService;
import com.stc.thamquan.utils.EnumRole;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 14:42
 * Filename  : GiangVienServiceImpl
 */
@Slf4j
@Service
public class GiangVienServiceImpl implements GiangVienService {
    private final GiangVienRepository giangVienRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final KhoaService khoaService;

    private final NganhService nganhService;

    private final HocHamService hocHamService;

    private final HocViService hocViService;

    private final LinhVucService linhVucService;

    private final MongoTemplate mongoTemplate;

    @Value("${default.password}")
    private String defaultPassword;

    public GiangVienServiceImpl(GiangVienRepository giangVienRepository,
                                VietnameseStringUtils vietnameseStringUtils, KhoaService khoaService,
                                NganhService nganhService, HocHamService hocHamService,
                                HocViService hocViService, LinhVucService linhVucService, MongoTemplate mongoTemplate) {

        this.giangVienRepository = giangVienRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.khoaService = khoaService;
        this.nganhService = nganhService;
        this.hocHamService = hocHamService;
        this.hocViService = hocViService;
        this.linhVucService = linhVucService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<GiangVien> filter(FilterGiangVienDto dto, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getSearch())) {
            searchCriterias.add(Criteria.where("maGiangVien").regex(vietnameseStringUtils.makeSearchRegex(dto.getSearch()), "i"));
            searchCriterias.add(Criteria.where("hoTen").regex(vietnameseStringUtils.makeSearchRegex(dto.getSearch()), "i"));
            searchCriterias.add(Criteria.where("email").regex(vietnameseStringUtils.makeSearchRegex(dto.getSearch()), "i"));
        }
        if (!ObjectUtils.isEmpty(dto.getKhoa())) {
            criteria.and("khoa.$id").is(new ObjectId(dto.getKhoa()));
        }
        if (!ObjectUtils.isEmpty(dto.getNganh())) {
            criteria.and("nganh.$id").is(new ObjectId(dto.getNganh()));
        }

        List<Criteria> linhVucCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getLinhVuc())) {
            linhVucCriterias.add(Criteria.where("linhVucs.maLinhVuc").regex(vietnameseStringUtils.makeSearchRegex(dto.getLinhVuc()), "i"));
            linhVucCriterias.add(Criteria.where("linhVucs.tenLinhVuc").regex(vietnameseStringUtils.makeSearchRegex(dto.getLinhVuc()), "i"));
        }
        if (searchCriterias.size() > 0) {
            criteria.orOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }
        if (linhVucCriterias.size() > 0) {
            Criteria linhVucCriteria = new Criteria().orOperator(linhVucCriterias.toArray(new Criteria[linhVucCriterias.size()]));
            criteria.andOperator(linhVucCriteria);
        }
        MatchOperation searchOperation = match(criteria);
        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort());
        FacetOperation facetOperation = facet(
                count().as("total")).as("total")
                .and(skipOperation, limit(pageable.getPageSize())
                ).as("metaData");
        Aggregation aggregation = newAggregation(
                searchOperation,
                sortOperation,
                facetOperation
        );
        AggregationResults<PageGiangVienTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                GiangVien.class,
                PageGiangVienTransfer.class);
        List<PageGiangVienTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public GiangVien getGiangVien(String id) {
        return giangVienRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Giảng viên có id %s không tồn tại", id)));
    }

    @Override
    public GiangVien getGiangVien(Principal principal) {
        return giangVienRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("Giảng viên có email %s không tồn tại", principal.getName())));
    }

    @Override
    public GiangVien getGiangVienByIdCore(String id) {
        return giangVienRepository.findByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public GiangVien createGiangVien(GiangVienDto dto, Principal principal) {
        if (ObjectUtils.isEmpty(dto.getMaGiangVien())) {
            throw new InvalidException("Mã giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new InvalidException("Email giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Họ tên giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getHocVi())) {
            throw new InvalidException("Học vị không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKhoa())) {
            throw new InvalidException("Khoa không được bỏ trống");
        }
        if (giangVienRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidException(String.format("Giảng viên có email %s đã tồn tại", dto.getEmail()));
        }
        if (giangVienRepository.existsByMaGiangVien(dto.getMaGiangVien())) {
            throw new InvalidException(String.format("Giảng viên có mã %s đã tồn tại", dto.getMaGiangVien()));
        }
        HocVi hocVi = hocViService.getHocVi(dto.getHocVi());
        if (hocVi == null) {
            throw new NotFoundException(String.format("Học vị có id %s không tồn tại", dto.getHocVi()));
        }
        Khoa khoa = khoaService.getKhoaByIdCore(dto.getKhoa());
        if (khoa == null) {
            throw new InvalidException(String.format("Khoa có id %s không tồn tại", dto.getKhoa()));
        }
        GiangVien giangVien = new GiangVien();
        giangVien.setMaGiangVien(dto.getMaGiangVien());
        giangVien.setEmail(dto.getEmail());
        giangVien.setHoTen(dto.getHoTen());
        // Học hàm có thê bỏ trống
        if (!ObjectUtils.isEmpty(dto.getHocHam())) {
            HocHam hocHam = hocHamService.getHocHam(dto.getHocHam());
            if (hocHam == null) {
                throw new NotFoundException(String.format("Học hàm có id %s không tồn tại", dto.getHocHam()));
            }
            giangVien.setHocHam(hocHam);
        }
        giangVien.setHocVi(hocVi);
        giangVien.setPassword(defaultPassword);
        giangVien.setKhoa(khoa);
        if (!ObjectUtils.isEmpty(dto.getNganh())) {
            Nganh nganh = nganhService.getNganhByIdCore(dto.getNganh());
            if (nganh == null) {
                throw new NotFoundException(String.format("Ngành có id %s không tồn tại", dto.getNganh()));
            }
            giangVien.setNganh(nganh);
        }
        if (!ObjectUtils.isEmpty(dto.getLinhVucs())) {
            List<LinhVuc> linhVucs = new ArrayList<>();
            dto.getLinhVucs().forEach(linhVucId -> {
                LinhVuc linhVuc = linhVucService.getLinhVucByIdCore(linhVucId);
                if (linhVuc == null) {
                    throw new NotFoundException(String.format("Lĩnh vực có id %s không tồn tại", linhVucId));
                }
                linhVucs.add(linhVuc);
            });
            giangVien.setLinhVucs(linhVucs);
        }
        giangVien.setThinhGiang(dto.isThinhGiang());
        giangVien.setRoles(Collections.singletonList(EnumRole.ROLE_GIANG_VIEN.name()));
        giangVienRepository.save(giangVien);
        return giangVien;
    }

    @Override
    public GiangVien updateGiangVien(String id, GiangVienDto dto, Principal principal) {
        GiangVien giangVien = getGiangVien(id);
        if (ObjectUtils.isEmpty(dto.getMaGiangVien())) {
            throw new InvalidException("Mã giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new InvalidException("Email giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Họ tên giảng viên không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getHocVi())) {
            throw new InvalidException("Học vị không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getKhoa())) {
            throw new InvalidException("Khoa không được bỏ trống");
        }
        if (!giangVien.getMaGiangVien().equalsIgnoreCase(dto.getMaGiangVien())
                && giangVienRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidException(String.format("Giảng viên có mã %s đã tồn tại", dto.getMaGiangVien()));
        }
        HocVi hocVi = hocViService.getHocVi(dto.getHocVi());
        if (hocVi == null) {
            throw new NotFoundException(String.format("Học vị có id %s không tồn tại", dto.getHocVi()));
        }
        Khoa khoa = khoaService.getKhoaByIdCore(dto.getKhoa());
        if (khoa == null) {
            throw new InvalidException(String.format("Khoa có id %s không tồn tại", dto.getKhoa()));
        }
        giangVien.setMaGiangVien(dto.getMaGiangVien());
//        giangVien.setEmail(dto.getEmail());
        giangVien.setHoTen(dto.getHoTen());
        // Học hàm có thê bỏ trống
        if (!ObjectUtils.isEmpty(dto.getHocHam())) {
            HocHam hocHam = hocHamService.getHocHam(dto.getHocHam());
            if (hocHam == null) {
                throw new NotFoundException(String.format("Học hàm có id %s không tồn tại", dto.getHocHam()));
            }
            giangVien.setHocHam(hocHam);
        }
        giangVien.setHocVi(hocVi);
        giangVien.setPassword(defaultPassword);
        giangVien.setKhoa(khoa);
        if (!ObjectUtils.isEmpty(dto.getNganh())) {
            Nganh nganh = nganhService.getNganhByIdCore(dto.getNganh());
            if (nganh == null) {
                throw new NotFoundException(String.format("Ngành có id %s không tồn tại", dto.getNganh()));
            }
            giangVien.setNganh(nganh);
        }
        if (!ObjectUtils.isEmpty(dto.getLinhVucs())) {
            List<LinhVuc> linhVucs = new ArrayList<>();
            dto.getLinhVucs().forEach(linhVucId -> {
                LinhVuc linhVuc = linhVucService.getLinhVucByIdCore(linhVucId);
                if (linhVuc == null) {
                    throw new NotFoundException(String.format("Lĩnh vực có id %s không tồn tại", linhVucId));
                }
                linhVucs.add(linhVuc);
            });
            giangVien.setLinhVucs(linhVucs);
        }
        giangVien.setThinhGiang(dto.isThinhGiang());
        giangVienRepository.save(giangVien);
        return giangVien;
    }

    @Override
    public GiangVien changeStatus(String id) {
        GiangVien giangVien = getGiangVien(id);
        giangVien.setTrangThai(!giangVien.isTrangThai());
        giangVienRepository.save(giangVien);
        return giangVien;
    }

    @Override
    public List<GiangVien> getAllGiangViensActive(String search) {
        return giangVienRepository.getAllGiangViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public GiangVien getCurrentGiangVien(String email) {
        return giangVienRepository.findByEmail(email).orElseThrow(() -> new InvalidException(""));
    }
}
