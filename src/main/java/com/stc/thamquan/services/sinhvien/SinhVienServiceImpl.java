package com.stc.thamquan.services.sinhvien;

import com.mongodb.BasicDBObject;
import com.stc.thamquan.dtos.sinhvien.FilterSinhVienDto;
import com.stc.thamquan.dtos.sinhvien.PageSinhVienTransfer;
import com.stc.thamquan.dtos.sinhvien.SinhVienDto;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.CongTacVien;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.NganhReposiroty;
import com.stc.thamquan.repositories.SinhVienRepository;
import com.stc.thamquan.services.chuyenthamquan.ChuyenThamQuanService;
import com.stc.thamquan.services.nganh.NganhService;
import com.stc.thamquan.utils.EnumRole;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 13:37
 * Filename  : SinhVienServiceImpl
 */
@Slf4j
@Service
public class SinhVienServiceImpl implements SinhVienService {
    private final SinhVienRepository sinhVienRepository;

    private final NganhReposiroty nganhReposiroty;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final MessageSource messageSource;

    private final NganhService nganhService;

    private final MongoTemplate mongoTemplate;


    public SinhVienServiceImpl(SinhVienRepository sinhVienRepository,
                               NganhReposiroty nganhReposiroty,
                               VietnameseStringUtils vietnameseStringUtils, MessageSource messageSource,
                               NganhService nganhService, MongoTemplate mongoTemplate) {
        this.sinhVienRepository = sinhVienRepository;
        this.nganhReposiroty = nganhReposiroty;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.messageSource = messageSource;
        this.nganhService = nganhService;
        this.mongoTemplate = mongoTemplate;
    }

    /***
     * @author: duong
     * @CreatedDate: 14-05-2021
     */
    @Override
    public Page<SinhVien> filter(FilterSinhVienDto dto, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getMaSV())) {
            searchCriterias.add(Criteria.where("maSV").regex(vietnameseStringUtils.makeSearchRegex(dto.getMaSV()), "i"));
        }
        if (!ObjectUtils.isEmpty(dto.getHoTen())) {
            searchCriterias.add(Criteria.where("hoTen").regex(vietnameseStringUtils.makeSearchRegex(dto.getHoTen()), "i"));
        }
        if (!ObjectUtils.isEmpty(dto.getEmail())) {
            searchCriterias.add(Criteria.where("email").regex(vietnameseStringUtils.makeSearchRegex(dto.getEmail()), "i"));
        }
        if (!ObjectUtils.isEmpty(dto.getKhoa())) {
            criteria.and("nganh.khoa.$id").is(new ObjectId(dto.getKhoa()));
        }
        if (!ObjectUtils.isEmpty(dto.getNganh())) {
            criteria.and("nganh._id").is(new ObjectId(dto.getNganh()));
        }
        if (searchCriterias.size() > 0) {
            criteria.orOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }
        MatchOperation matchOperation = match(criteria);

        ProjectionOperation projectionOperation = Aggregation.project("maSV", "cmnd", "hoTen", "email",
                "lop", "gioiTinh", "dienThoai", "ngaySinh").and(ConvertOperators.ToObjectId.toObjectId("$nganh.$id")).as("nganhId");
        LookupOperation lookupOperation = lookup(
                "nganh",
                "nganhId",
                "_id",
                "nganh"
        );

        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort());
        FacetOperation facetOperation = facet(
                count().as("total")).as("total")
                .and(skipOperation, limit(pageable.getPageSize())
                ).as("metaData");
        Aggregation aggregation = newAggregation(
                projectionOperation,
                lookupOperation,
                matchOperation,
                sortOperation,
                facetOperation
        );
        AggregationResults<PageSinhVienTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                SinhVien.class,
                PageSinhVienTransfer.class);
        List<PageSinhVienTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public SinhVien getSinhVien(String id) {
        return sinhVienRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Sinh viên có id %s không tồn tại", id)));
    }

    @Override
    public SinhVien getCurrentSinhVien(Principal principal) {
        return sinhVienRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("Sinh viên có email %s không tồn tại", principal.getName())));
    }

    @Override
    public SinhVien getSinhVienByIdCore(String id) {
        return sinhVienRepository.findByIdAndTrangThaiTrue(id).orElse(null);
    }

    @Override
    public SinhVien createSinhVien(SinhVienDto dto) {
        SinhVien sinhVien = new SinhVien();
        if (ObjectUtils.isEmpty(dto.getMaSV())) {
            throw new InvalidException("Mã sinh viên không được bỏ trống!");
        }
        if (ObjectUtils.isEmpty(dto.getHoTen())) {
            throw new InvalidException("Họ tên sinh viên không được bỏ trống!");
        }
        if (!ObjectUtils.isEmpty(dto.getCmnd())) {
            sinhVien.setCmnd(dto.getCmnd());
        }
        if (!ObjectUtils.isEmpty(dto.getNgaySinh())) {
            sinhVien.setNgaySinh(dto.getNgaySinh());
        }
        if (!ObjectUtils.isEmpty(dto.getLop())) {
            sinhVien.setLop(dto.getLop());
        }
        if (!ObjectUtils.isEmpty(dto.isGioiTinh())) {
            sinhVien.setGioiTinh(dto.isGioiTinh());
        }
        if (!ObjectUtils.isEmpty(dto.getDienThoai())) {
            sinhVien.setDienThoai(dto.getDienThoai());
        }
        if (ObjectUtils.isEmpty(dto.getNganh())) {
            throw new InvalidException("Ngành không được bỏ trống!");
        }
        if (!ObjectUtils.isEmpty(dto.getPassword())) {
            sinhVien.setPassword(dto.getPassword());
        }
        if (sinhVienRepository.existsByMaSV(dto.getMaSV())) {
            throw new InvalidException(String.format("Sinh viên có mã sinh viên %s đã tồn tại", dto.getMaSV()));
        }
        String email = dto.getMaSV() + "@student.hcmute.edu.vn";
        sinhVien.setMaSV(dto.getMaSV());
        sinhVien.setHoTen(dto.getHoTen());
        sinhVien.setEmail(email);
        sinhVien.setNganh(nganhService.getNganh(dto.getNganh()));
        sinhVien.setRoles(Collections.singletonList(EnumRole.ROLE_SINH_VIEN.name()));
        sinhVien.setTrangThai(true);
        sinhVienRepository.save(sinhVien);
        return sinhVien;
    }

    @Override
    public SinhVien updateSinhVien(String id, SinhVienDto dto) {
        SinhVien sinhVien = getSinhVien(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (!ObjectUtils.isEmpty(dto.getMaSV())) {
                if (sinhVienRepository.existsByMaSV(dto.getMaSV()) && !sinhVien.getMaSV().equals(dto.getMaSV())) {
                    throw new InvalidException(String.format("Sinh viên có mã sinh viên %s đã tồn tại", dto.getMaSV()));
                }
                sinhVien.setMaSV(dto.getMaSV());
                String emailSV = dto.getMaSV() + "@student.hcmute.edu.vn";
                sinhVien.setEmail(emailSV);
            }
        }
        if (!ObjectUtils.isEmpty(dto.getHoTen())) {
            sinhVien.setHoTen(dto.getHoTen());
        }
        if (!ObjectUtils.isEmpty(dto.getNgaySinh())) {
            sinhVien.setNgaySinh(dto.getNgaySinh());
        }
        if (!ObjectUtils.isEmpty(dto.getDienThoai())) {
            sinhVien.setDienThoai(dto.getDienThoai());
        }
        if (!ObjectUtils.isEmpty(dto.isGioiTinh())) {
            sinhVien.setGioiTinh(dto.isGioiTinh());
        }
        if (!ObjectUtils.isEmpty(dto.getNganh())) {
            sinhVien.setNganh(nganhService.getNganh(dto.getNganh()));
        }
        if (!ObjectUtils.isEmpty(dto.getLop())) {
            sinhVien.setLop(dto.getLop());
        }
        if (!ObjectUtils.isEmpty(dto.getCmnd())) {
            sinhVien.setCmnd(dto.getCmnd());
        }
        if (!ObjectUtils.isEmpty(dto.getPassword())) {
            sinhVien.setPassword(dto.getPassword());
        }
        sinhVien.setRoles(Collections.singletonList(EnumRole.ROLE_SINH_VIEN.name()));
        sinhVien.setTrangThai(true);
        sinhVienRepository.save(sinhVien);
        return sinhVien;
    }

    @Override
    public SinhVien changeStatus(String id) {
        SinhVien sinhVien = getSinhVien(id);
        sinhVien.setTrangThai(!sinhVien.isTrangThai());
        sinhVienRepository.save(sinhVien);
        return sinhVien;
    }

    @Override
    public List<SinhVien> getAllSinhViensActive(String search) {
        return sinhVienRepository.getAllSinhViensActive(vietnameseStringUtils.makeSearchRegex(search));
    }

}
