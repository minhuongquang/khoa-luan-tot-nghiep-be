package com.stc.thamquan.services.baiviet;

import com.stc.thamquan.dtos.baiviet.BaiVietDto;
import com.stc.thamquan.dtos.baiviet.PageBaiVietTransfer;
import com.stc.thamquan.entities.BaiViet;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.BaiVietRepository;
import com.stc.thamquan.services.myfile.MyFileService;
import com.stc.thamquan.utils.EnumLoaiBaiViet;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:26
 * Filename  : BaiVietServiceImpl
 */
@Slf4j
@Service
public class BaiVietServiceImpl implements BaiVietService {
    private final BaiVietRepository baiVietRepository;

    private final MyFileService myFileService;

    private final MongoTemplate mongoTemplate;

    private final MessageSource messageSource;

    private final VietnameseStringUtils vietnameseStringUtils;

    public BaiVietServiceImpl(BaiVietRepository baiVietRepository, MyFileService myFileService,
                              MongoTemplate mongoTemplate, MessageSource messageSource,
                              VietnameseStringUtils vietnameseStringUtils) {
        this.baiVietRepository = baiVietRepository;
        this.myFileService = myFileService;
        this.mongoTemplate = mongoTemplate;
        this.messageSource = messageSource;
        this.vietnameseStringUtils = vietnameseStringUtils;
    }

    @Override
    public List<String> getLoaiBaiViets() {
        return Arrays.stream(EnumLoaiBaiViet.values()).map(Enum::name).sorted().collect(Collectors.toList());
    }

    @Override
    public Page<BaiViet> filter(String search, String loaiBaiViet, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(search)) {
            searchCriterias.add(Criteria.where("tieuDe").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("tieuDeEn").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("noiDung").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("noiDungEn").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
        }
        if (!ObjectUtils.isEmpty(loaiBaiViet)) {
            criteria.and("loaiBaiViet").regex(vietnameseStringUtils.makeSearchRegex(loaiBaiViet), "i");
        }
        criteria.and("trangThai").is(true);
        if (searchCriterias.size() > 0) {
            criteria.orOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }

        MatchOperation searchOperation = match(criteria);
        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort())
                .and(Sort.Direction.ASC, "tieuDe")
                .and(Sort.Direction.ASC, "tieuDeEn");
        FacetOperation facetOperation = facet(
                count().as("total")).as("total")
                .and(skipOperation, limit(pageable.getPageSize())
                ).as("metaData");
        Aggregation aggregation = newAggregation(
                searchOperation,
                sortOperation,
                facetOperation
        );
        AggregationResults<PageBaiVietTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                BaiViet.class,
                PageBaiVietTransfer.class);
        List<PageBaiVietTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public Page<BaiViet> paging(String search, String loaiBaiViet, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(search)) {
            searchCriterias.add(Criteria.where("tieuDe").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("tieuDeEn").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("noiDung").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("noiDungEn").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
        }
        if (!ObjectUtils.isEmpty(loaiBaiViet)) {
            criteria.and("loaiBaiViet").regex(vietnameseStringUtils.makeSearchRegex(loaiBaiViet), "i");
        }
        criteria.and("trangThai").is(true);
        if (searchCriterias.size() > 0) {
            criteria.orOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }

        MatchOperation searchOperation = match(criteria);
        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort())
                .and(Sort.Direction.ASC, "tieuDeEn");
        FacetOperation facetOperation = facet(
                count().as("total")).as("total")
                .and(skipOperation, limit(pageable.getPageSize())
                ).as("metaData");
        Aggregation aggregation = newAggregation(
                searchOperation,
                sortOperation,
                facetOperation
        );
        AggregationResults<PageBaiVietTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                BaiViet.class,
                PageBaiVietTransfer.class);
        List<PageBaiVietTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public BaiViet getBaiViet(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return baiVietRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(messageSource.getMessage("error.baivietnotfound", null, locale), id)));
    }

    @Override
    public BaiViet createBaiViet(BaiVietDto dto) {
        BaiViet baiViet = new BaiViet();

        baiViet.setThuTu(dto.getThuTu());
        baiViet.setLoaiBaiViet(dto.getLoaiBaiViet());
        baiViet.setFileAnhBia(dto.getFileAnhBia());
        baiViet.setTieuDe(dto.getTieuDe());
        baiViet.setTieuDeEn(dto.getTieuDeEn());
        baiViet.setNoiDung(dto.getNoiDung());
        baiViet.setNoiDungEn(dto.getNoiDungEn());

        baiVietRepository.save(baiViet);
        return baiViet;
    }

    @Override
    public BaiViet updateBaiViet(String id, BaiVietDto dto) {
        BaiViet baiViet = getBaiViet(id);

        baiViet.setThuTu(dto.getThuTu());
        baiViet.setLoaiBaiViet(dto.getLoaiBaiViet());
        baiViet.setFileAnhBia(dto.getFileAnhBia());
        baiViet.setTieuDe(dto.getTieuDe());
        baiViet.setTieuDeEn(dto.getTieuDeEn());
        baiViet.setNoiDung(dto.getNoiDung());
        baiViet.setNoiDungEn(dto.getNoiDungEn());
        baiVietRepository.save(baiViet);
        return baiViet;
    }

    @Override
    public BaiViet changeStatus(String id) {
        BaiViet baiViet = getBaiViet(id);
        baiViet.setTrangThai(!baiViet.isTrangThai());
        baiVietRepository.save(baiViet);
        return baiViet;
    }
}
