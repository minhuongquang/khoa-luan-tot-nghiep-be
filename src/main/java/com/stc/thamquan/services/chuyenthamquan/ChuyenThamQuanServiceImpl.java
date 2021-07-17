package com.stc.thamquan.services.chuyenthamquan;

import com.stc.thamquan.dtos.EmailDto;
import com.stc.thamquan.dtos.chuyenthamquan.*;
import com.stc.thamquan.dtos.kafka.TextEmail;
import com.stc.thamquan.dtos.sinhvien.PageSinhVienThamQuanTransfer;
import com.stc.thamquan.dtos.sinhvien.PageSinhVienTransfer;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.CongTacVienDanDoan;
import com.stc.thamquan.entities.embedded.GPS;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.feigns.ScheduleServiceClient;
import com.stc.thamquan.repositories.ChuyenThamQuanRepository;
import com.stc.thamquan.repositories.SinhVienRepository;
import com.stc.thamquan.services.cauhinhhethong.CauHinhHeThongService;
import com.stc.thamquan.services.congtacvien.CongTacVienService;
import com.stc.thamquan.services.diadiem.DiaDiemService;
import com.stc.thamquan.services.doanhnghiep.DoanhNghiepService;
import com.stc.thamquan.services.dotthamquan.DotThamQuanService;
import com.stc.thamquan.services.filestore.FileStorageService;
import com.stc.thamquan.services.myfile.MyFileService;
import com.stc.thamquan.services.phuongtien.PhuongTienService;
import com.stc.thamquan.services.email.EmailService;
import com.stc.thamquan.services.excel.ExcelService;
import com.stc.thamquan.services.taikhoan.TaiKhoanService;
import com.stc.thamquan.services.word.WordService;
import com.stc.thamquan.services.giangvien.GiangVienService;
import com.stc.thamquan.services.sinhvien.SinhVienService;
import com.stc.thamquan.utils.EnumTrangThai;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.security.Principal;
import java.io.File;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.Locale;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:19
 * Filename  : ChuyenThamQuanServiceImpl
 */
@Slf4j
@Service
public class ChuyenThamQuanServiceImpl implements ChuyenThamQuanService {

    private DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private final ChuyenThamQuanRepository chuyenThamQuanRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final ExcelService excelService;

    private final CauHinhHeThongService cauHinhHeThongService;

    private final SinhVienService sinhVienService;

    private final SinhVienRepository sinhVienRepository;

    private final DotThamQuanService dotThamQuanService;

    private final CongTacVienService congTacVienService;

    private final DoanhNghiepService doanhNghiepService;

    private final GiangVienService giangVienService;

    private final WordService wordService;

    private final MongoTemplate mongoTemplate;

    private final EmailService emailService;

    private final FileStorageService fileStorageService;

    private final MyFileService myFileService;

    private final PhuongTienService phuongTienService;

    private final DiaDiemService diaDiemService;

    private final ScheduleServiceClient scheduleServiceClient;

    private final Configuration freemarkerConfig;

    private final TaiKhoanService taiKhoanService;

    private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Value("${spring.application.name}")
    private String cliendId;

    public ChuyenThamQuanServiceImpl(ChuyenThamQuanRepository chuyenThamQuanRepository,
                                     VietnameseStringUtils vietnameseStringUtils,
                                     CauHinhHeThongService cauHinhHeThongService, ExcelService excelService, EmailService emailService,
                                     GiangVienService giangVienService, CongTacVienService congTacVienService,
                                     SinhVienService sinhVienService, DotThamQuanService dotThamQuanService,
                                     DoanhNghiepService doanhNghiepService, WordService wordService,
                                     MongoTemplate mongoTemplate, FileStorageService fileStorageService,
                                     MyFileService myFileService, SinhVienRepository sinhVienRepository,
                                     PhuongTienService phuongTienService, DiaDiemService diaDiemService,
                                     @Qualifier("com.stc.thamquan.feigns.ScheduleServiceClient") ScheduleServiceClient scheduleServiceClient,
                                     Configuration freemarkerConfig, TaiKhoanService taiKhoanService) {
        this.chuyenThamQuanRepository = chuyenThamQuanRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.cauHinhHeThongService = cauHinhHeThongService;
        this.sinhVienRepository = sinhVienRepository;
        this.excelService = excelService;
        this.sinhVienService = sinhVienService;
        this.giangVienService = giangVienService;
        this.emailService = emailService;
        this.congTacVienService = congTacVienService;
        this.dotThamQuanService = dotThamQuanService;
        this.doanhNghiepService = doanhNghiepService;
        this.wordService = wordService;
        this.mongoTemplate = mongoTemplate;
        this.fileStorageService = fileStorageService;
        this.myFileService = myFileService;
        this.phuongTienService = phuongTienService;
        this.diaDiemService = diaDiemService;
        this.scheduleServiceClient = scheduleServiceClient;
        this.freemarkerConfig = freemarkerConfig;
        this.taiKhoanService = taiKhoanService;
    }

    @Override
    public Page<ChuyenThamQuan> filter(FilterChuyenThamQuanDto dto, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getTenChuyenThamQuan())) {
            searchCriterias.add(Criteria.where("tenChuyenThamQuan").regex(vietnameseStringUtils.makeSearchRegex(dto.getTenChuyenThamQuan()), "i"));
        }
        List<Criteria> trangThaiCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getTrangThai())) {
            dto.getTrangThai().stream().map(tt -> trangThaiCriterias.add(Criteria.where("trangThai").regex(vietnameseStringUtils.makeSearchRegex(tt), "i"))).collect(Collectors.toList());
        }
        if (trangThaiCriterias.size() > 0) {
            Criteria trangThaiCriteria = new Criteria().orOperator(trangThaiCriterias.toArray(new Criteria[trangThaiCriterias.size()]));
            criteria.andOperator(trangThaiCriteria);

        }
        if (!ObjectUtils.isEmpty(dto.getNamHoc())) {
            searchCriterias.add(Criteria.where("dotThamQuans.namHoc").regex(vietnameseStringUtils.makeSearchRegex(dto.getNamHoc()), "i"));
        }
        if (!ObjectUtils.isEmpty(dto.getHocKy())) {
            searchCriterias.add(Criteria.where("dotThamQuans.hocKy").regex(vietnameseStringUtils.makeSearchRegex(dto.getHocKy()), "i"));
        }
        if (dto.getDotThamQuan() == null && dto.isChuyenMoi()) {
            criteria.and("dotThamQuan").exists(false);
        }
        if (!ObjectUtils.isEmpty(dto.getDotThamQuan())) {
            criteria.and("dotThamQuan.$id").is(new ObjectId(dto.getDotThamQuan()));
        }
        if (!ObjectUtils.isEmpty(dto.getDoanhNghiep()))
            criteria.and("doanhNghiep.$id").is(new ObjectId(dto.getDoanhNghiep()));

        if (!ObjectUtils.isEmpty(dto.getThoiGianBatDau()) && !ObjectUtils.isEmpty(dto.getThoiGianKetThuc())) {
            criteria.and("thoiGianBatDauThamQuan").gte(dto.getThoiGianBatDau());
            criteria.and("thoiGianKetThucThamQuan").lte(dto.getThoiGianBatDau());
        } else {
            if (!ObjectUtils.isEmpty(dto.getThoiGianBatDau())) {
                criteria.and("thoiGianBatDauThamQuan").gte(dto.getThoiGianBatDau());
            }
            if (!ObjectUtils.isEmpty(dto.getThoiGianKetThuc())) {
                criteria.and("thoiGianKetThucThamQuan").lte(dto.getThoiGianBatDau());
            }
        }

        if (!ObjectUtils.isEmpty(dto.getGiangVien())) {
            criteria.and("giangVienDangKy.$id").is(new ObjectId(dto.getGiangVien()));
        }

        List<Criteria> congTacVienCriterias = new ArrayList<>();
        if (!ObjectUtils.isEmpty(dto.getCongTacVien())) {
            if (ObjectId.isValid(dto.getCongTacVien())) {
                congTacVienCriterias.add(Criteria.where("congTacViens._id").is(new ObjectId(dto.getCongTacVien())));
            }
            congTacVienCriterias.add(Criteria.where("congTacViens.email").regex(vietnameseStringUtils.makeSearchRegex(dto.getCongTacVien()), "i"));
            congTacVienCriterias.add(Criteria.where("congTacViens.hoTen").regex(vietnameseStringUtils.makeSearchRegex(dto.getCongTacVien()), "i"));
        }
        if (searchCriterias.size() > 0) {
            criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }
        if (congTacVienCriterias.size() > 0) {
            Criteria congTacVienCriteria = new Criteria().orOperator(congTacVienCriterias.toArray(new Criteria[congTacVienCriterias.size()]));
            criteria.andOperator(congTacVienCriteria);
        }
        ProjectionOperation projectionOperation = Aggregation.project("thuTu",
                "tenChuyenThamQuan", "noiDungThamQuan", "thoiGianKhoiHanh", "diaDiemKhoiHanh",
                "phuongTien", "dotThamQuan", "giangVienDangKy", "doanhNghiep",
                "thoiGianDuKien", "danhSachGiangVienThamGias", "thoiGianBatDauThamQuan", "thoiGianKetThucThamQuan",
                "danhSachSinhViens", "congTacViens",
                "phuCapCongTacVien", "kinhPhi", "thueXeNgoaiTruong", "fileScanGiayXacNhanDoanhNghiep",
                "fileScanKeHoach", "duongDan", "fileXacNhanDuyetTuBanGiamHieu", "thoiHanLamKhaoSat",
                "trangThai", "createdDate")
                .and(ConvertOperators.ToObjectId.toObjectId("$dotThamQuan.$id")).as("dotThamQuanId");
        LookupOperation lookupOperation = lookup(
                "dot-tham-quan",
                "dotThamQuanId",
                "_id",
                "dotThamQuans");
        MatchOperation matchOperation = match(criteria);
        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort()).and(Sort.Direction.ASC, "doanhNghiep.congTy");
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
        AggregationResults<PageChuyenThamQuanTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                ChuyenThamQuan.class,
                PageChuyenThamQuanTransfer.class);
        List<PageChuyenThamQuanTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public Page<ChuyenThamQuan> getChuyenThamQuanPaging(FilterChuyenThamQuanDto dto, int page, int size, String sort, String column) {
        return filter(dto, page, size, sort, column);
    }

    @Override
    public List<ChuyenThamQuan> getAllChuyenThamQuansActive(String search) {
        return chuyenThamQuanRepository.getAllChuyenThamQuansByTrangThaiHoanTat(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public ChuyenThamQuan getChuyenThamQuan(String id) {
        return chuyenThamQuanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Chuyến tham quan có id %s không tồn tại", id)));
    }

    @Override
    public ChuyenThamQuan createChuyenThamQuan(ChuyenThamQuanDto dto, boolean duyet, Principal principal) {
        ChuyenThamQuan chuyenThamQuan = new ChuyenThamQuan();
        GiangVien giangVien;
        if (ObjectUtils.isEmpty(dto.getTenChuyenThamQuan()))
            throw new InvalidException("Tên chuyến tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getDiaDiemKhoiHanh()))
            throw new InvalidException("Địa điểm khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKhoiHanh()))
            throw new InvalidException("Thời gian khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getKinhPhi()))
            throw new InvalidException("Kinh phí không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianBatDauThamQuan()))
            throw new InvalidException("Thời gian bắt đầu tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKetThucThamQuan()))
            throw new InvalidException("Thời gian kết thúc tham quan không được bỏ trống");

        DiaDiem diaDiem = diaDiemService.getDiaDiem(dto.getDiaDiemKhoiHanh());

        // doanhnghiep
        if (!ObjectUtils.isEmpty(dto.getDoanhNghiep())) {
            chuyenThamQuan.setDoanhNghiep(doanhNghiepService.getDoanhNghiep(dto.getDoanhNghiep()));
        }

        // phương tiện
        if (!ObjectUtils.isEmpty(dto.getPhuongTien())) {
            List<PhuongTien> phuongTienList = new ArrayList<>();
            dto.getPhuongTien().stream().map(pt -> phuongTienList.add(phuongTienService.getPhuongTien(pt))).collect(Collectors.toList());
            chuyenThamQuan.setPhuongTien(phuongTienList);
        }

        //sinhvien
        if (!ObjectUtils.isEmpty(dto.getDanhSachSinhViens())) {
            List<SinhVienThamQuan> sinhVienThamQuans = new ArrayList<>();
            dto.getDanhSachSinhViens().forEach(idSV -> {
                SinhVien sinhVien = sinhVienService.getSinhVienByIdCore(idSV);
                SinhVienThamQuan sinhVienThamQuan = sinhVienThamQuans.stream()
                        .filter(svtq -> svtq.getSinhVien().equals(sinhVien))
                        .findAny().orElse(null);
                if (sinhVien != null && !sinhVienThamQuans.contains(sinhVienThamQuan)) {
                    sinhVienThamQuans.add(new SinhVienThamQuan(sinhVien));
                }

            });
            chuyenThamQuan.setDanhSachSinhViens(sinhVienThamQuans);
        }

        if (ObjectUtils.isEmpty(dto.getDoanhNghiep())) {
            chuyenThamQuan.setTrangThai(EnumTrangThai.GIANG_VIEN_CHUA_CO_CONG_TY.name());
        } else {
            chuyenThamQuan.setTrangThai(EnumTrangThai.GIANG_VIEN_DA_CHON_CONG_TY.name());
        }
        // kiem tra role giang vien
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GIANG_VIEN"))) {
            giangVien = giangVienService.getCurrentGiangVien(principal.getName());
        } else {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByEmail(principal.getName());
            if (!ObjectUtils.isEmpty(dto.getDotThamQuan())) {
                DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(dto.getDotThamQuan());
                chuyenThamQuan.setDotThamQuan(dotThamQuan);
            }

            giangVien = giangVienService.getGiangVienByIdCore(dto.getGiangVienDangKy());

            // trang thai tour
            if (duyet) {
                chuyenThamQuan.setTrangThai(EnumTrangThai.DANG_XU_LY.name());
            }
            // congtacvien
            if (!ObjectUtils.isEmpty(dto.getCongTacViens())) {
                List<CongTacVienDanDoan> congTacVienDanDoans = new ArrayList<>();
                dto.getCongTacViens().forEach(idCTV -> {
                    CongTacVienDanDoan congTacVienDanDoan1 = congTacVienDanDoans.stream()
                            .filter(ctvdd -> ctvdd.getCongTacVien().getId().equals(idCTV))
                            .findAny().orElse(null);
                    if (!congTacVienDanDoans.contains(congTacVienDanDoan1)) {
                        CongTacVien congTacVien = congTacVienService.getCongTacVienByIdCore(idCTV);
                        if (congTacVien != null) {
                            CongTacVienDanDoan congTacVienDanDoan = new CongTacVienDanDoan();
                            congTacVienDanDoan.setCongTacVien(congTacVien);
                            congTacVienDanDoan.setNgayDuyet(new LocalDateTime().toDate());
                            congTacVienDanDoan.setNguoiDuyet(taiKhoan);
                            congTacVienDanDoan.setTrangThai(true);
                            congTacVienDanDoans.add(congTacVienDanDoan);
                        }
                    }

                });
                chuyenThamQuan.setCongTacViens(congTacVienDanDoans);
            }
        }
        chuyenThamQuan.setTenChuyenThamQuan(dto.getTenChuyenThamQuan());
        chuyenThamQuan.setThoiGianKhoiHanh(dto.getThoiGianKhoiHanh());
        chuyenThamQuan.setNoiDungThamQuan(dto.getNoiDungThamQuan());
        chuyenThamQuan.setDiaDiemKhoiHanh(diaDiem);
        chuyenThamQuan.setThoiGianBatDauThamQuan(dto.getThoiGianBatDauThamQuan());
        chuyenThamQuan.setThoiGianKetThucThamQuan(dto.getThoiGianKetThucThamQuan());
        chuyenThamQuan.setThoiHanLamKhaoSat(new DateTime(dto.getThoiGianKetThucThamQuan()).plusDays(1).withTimeAtStartOfDay().toDate());
        chuyenThamQuan.setPhuCapCongTacVien(dto.getPhuCapCongTacVien());
        chuyenThamQuan.setKinhPhi(dto.getKinhPhi());
        chuyenThamQuan.setThueXeNgoaiTruong(dto.isThueXeNgoaiTruong());
        chuyenThamQuan.setGiangVienDangKy(giangVien);
        chuyenThamQuan.setThoiGianDuKien(new Date());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;

    }

    @Override
    public ChuyenThamQuan updateChuyenThamQuan(String id, ChuyenThamQuanDto dto, Principal principal) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GIANG_VIEN"))) {
            if (chuyenThamQuan.getTrangThai().equals("DANG_XU_LY")) {
                throw new InvalidException("Chuyến tham quan đang trong quá trình xử lý. Giảng viên không thể chỉnh sửa thông tin. Vui lòng liên hệ phòng Quan hệ Doanh nghiệp để chỉnh sửa.");
            }
        } else {
            TaiKhoan taiKhoan = taiKhoanService.getCurrent(principal);
            if (!ObjectUtils.isEmpty(dto.getDotThamQuan())) {
                DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(dto.getDotThamQuan());
                chuyenThamQuan.setDotThamQuan(dotThamQuan);
            }
            // congtacvien
            if (!ObjectUtils.isEmpty(dto.getCongTacViens())) {
                List<CongTacVienDanDoan> congTacVienDanDoans = new ArrayList<>();
                dto.getCongTacViens().forEach(idCTV -> {
                    CongTacVienDanDoan congTacVienDanDoan1 = congTacVienDanDoans.stream()
                            .filter(ctvdd -> ctvdd.getCongTacVien().getId().equals(idCTV))
                            .findAny().orElse(null);
                    if (!congTacVienDanDoans.contains(congTacVienDanDoan1)) {
                        CongTacVien congTacVien = congTacVienService.getCongTacVienByIdCore(idCTV);
                        if (congTacVien != null) {
                            CongTacVienDanDoan congTacVienDanDoan = new CongTacVienDanDoan();
                            congTacVienDanDoan.setCongTacVien(congTacVien);
                            congTacVienDanDoan.setNgayDuyet(new LocalDateTime().toDate());
                            congTacVienDanDoan.setNguoiDuyet(taiKhoan);
                            congTacVienDanDoan.setTrangThai(true);
                            congTacVienDanDoans.add(congTacVienDanDoan);
                        }
                    }

                });
                chuyenThamQuan.setCongTacViens(congTacVienDanDoans);
            }
            if (!ObjectUtils.isEmpty(dto.getGiangVienDangKy())) {
                chuyenThamQuan.setGiangVienDangKy(giangVienService.getGiangVienByIdCore(dto.getGiangVienDangKy()));

            }
        }


        if (ObjectUtils.isEmpty(dto.getTenChuyenThamQuan()))
            throw new InvalidException("Tên chuyến tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getDiaDiemKhoiHanh()))
            throw new InvalidException("Địa điểm khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKhoiHanh()))
            throw new InvalidException("Thời gian khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getKinhPhi()))
            throw new InvalidException("Kinh phí không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianBatDauThamQuan()))
            throw new InvalidException("Thời gian bắt đầu tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKetThucThamQuan()))
            throw new InvalidException("Thời gian kết thúc tham quan không được bỏ trống");

        DiaDiem diaDiem = diaDiemService.getDiaDiem(dto.getDiaDiemKhoiHanh());
        // doanhnghiep
        if (!ObjectUtils.isEmpty(dto.getDoanhNghiep())) {
            chuyenThamQuan.setDoanhNghiep(doanhNghiepService.getDoanhNghiep(dto.getDoanhNghiep()));
        }

        //sinhvien
        if (!ObjectUtils.isEmpty(dto.getDanhSachSinhViens())) {
            List<SinhVienThamQuan> sinhVienThamQuans = new ArrayList<>();
            dto.getDanhSachSinhViens().forEach(idSV -> {
                SinhVien sinhVien = sinhVienService.getSinhVienByIdCore(idSV);
                SinhVienThamQuan sinhVienThamQuan = sinhVienThamQuans.stream()
                        .filter(svtq -> svtq.getSinhVien().equals(sinhVien))
                        .findAny().orElse(null);
                if (sinhVien != null && !sinhVienThamQuans.contains(sinhVienThamQuan)) {
                    sinhVienThamQuans.add(new SinhVienThamQuan(sinhVien));
                }

            });
            chuyenThamQuan.setDanhSachSinhViens(sinhVienThamQuans);
        }


        // phuong tien
        if (!ObjectUtils.isEmpty(dto.getPhuongTien())) {
            List<PhuongTien> phuongTienList = new ArrayList<>();
            dto.getPhuongTien().stream().map(pt -> phuongTienList.add(phuongTienService.getPhuongTien(pt))).collect(Collectors.toList());
            chuyenThamQuan.setPhuongTien(phuongTienList);
        }

        if (!ObjectUtils.isEmpty(dto.getPhuCapCongTacVien())) {
            chuyenThamQuan.setPhuCapCongTacVien(dto.getPhuCapCongTacVien());
        }
        chuyenThamQuan.setTenChuyenThamQuan(dto.getTenChuyenThamQuan());
        chuyenThamQuan.setThoiGianKhoiHanh(dto.getThoiGianKhoiHanh());
        chuyenThamQuan.setNoiDungThamQuan(dto.getNoiDungThamQuan());
        chuyenThamQuan.setDiaDiemKhoiHanh(diaDiem);
        chuyenThamQuan.setThoiGianBatDauThamQuan(dto.getThoiGianBatDauThamQuan());
        chuyenThamQuan.setThoiGianKetThucThamQuan(dto.getThoiGianKetThucThamQuan());
        chuyenThamQuan.setKinhPhi(dto.getKinhPhi());
        chuyenThamQuan.setThueXeNgoaiTruong(dto.isThueXeNgoaiTruong());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan deleteChuyenThamQuan(String id) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        chuyenThamQuan.setTrangThai(EnumTrangThai.HUY.name());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan adminDuyetChuyenThamQuan(String id, ChuyenThamQuanDto dto, Principal principal) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        TaiKhoan taiKhoan = taiKhoanService.getCurrent(principal);
        if (!ObjectUtils.isEmpty(dto.getDotThamQuan())) {
            DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(dto.getDotThamQuan());
            chuyenThamQuan.setDotThamQuan(dotThamQuan);
        }
        if (ObjectUtils.isEmpty(dto.getTenChuyenThamQuan()))
            throw new InvalidException("Tên chuyến tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getDiaDiemKhoiHanh()))
            throw new InvalidException("Địa điểm khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKhoiHanh()))
            throw new InvalidException("Thời gian khởi hành không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getKinhPhi()))
            throw new InvalidException("Kinh phí không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianBatDauThamQuan()))
            throw new InvalidException("Thời gian bắt đầu tham quan không được bỏ trống");
        if (ObjectUtils.isEmpty(dto.getThoiGianKetThucThamQuan()))
            throw new InvalidException("Thời gian kết thúc tham quan không được bỏ trống");

        if (!ObjectUtils.isEmpty(dto.getDoanhNghiep()))
            chuyenThamQuan.setDoanhNghiep(doanhNghiepService.getDoanhNghiep(dto.getDoanhNghiep()));

        DiaDiem diaDiem = diaDiemService.getDiaDiem(dto.getDiaDiemKhoiHanh());
        // doanhnghiep
        if (!ObjectUtils.isEmpty(dto.getDoanhNghiep())) {
            chuyenThamQuan.setDoanhNghiep(doanhNghiepService.getDoanhNghiep(dto.getDoanhNghiep()));
        }
        //sinhvien
        if (!ObjectUtils.isEmpty(dto.getDanhSachSinhViens())) {
            List<SinhVienThamQuan> sinhVienThamQuans = new ArrayList<>();
            dto.getDanhSachSinhViens().forEach(idSV -> {
                SinhVien sinhVien = sinhVienService.getSinhVienByIdCore(idSV);
                if (!sinhVienThamQuans.contains(sinhVien) && sinhVien != null) {
                    sinhVienThamQuans.add(new SinhVienThamQuan(sinhVien));
                }

            });
            chuyenThamQuan.setDanhSachSinhViens(sinhVienThamQuans);
        }
        // congtacvien
        if (!ObjectUtils.isEmpty(dto.getCongTacViens())) {
            List<CongTacVienDanDoan> congTacVienDanDoans = new ArrayList<>();
            dto.getCongTacViens().forEach(idCTV -> {
                CongTacVienDanDoan congTacVienDanDoan1 = congTacVienDanDoans.stream()
                        .filter(ctvdd -> ctvdd.getCongTacVien().getId().equals(idCTV))
                        .findAny().orElse(null);
                if (!congTacVienDanDoans.contains(congTacVienDanDoan1)) {
                    CongTacVien congTacVien = congTacVienService.getCongTacVienByIdCore(idCTV);
                    if (congTacVien != null) {
                        CongTacVienDanDoan congTacVienDanDoan = new CongTacVienDanDoan();
                        congTacVienDanDoan.setCongTacVien(congTacVien);
                        congTacVienDanDoan.setNgayDuyet(new LocalDateTime().toDate());
                        congTacVienDanDoan.setNguoiDuyet(taiKhoan);
                        congTacVienDanDoan.setTrangThai(true);
                        congTacVienDanDoans.add(congTacVienDanDoan);
                    }
                }

            });
            chuyenThamQuan.setCongTacViens(congTacVienDanDoans);
        }
        // phuong tien
        if (!ObjectUtils.isEmpty(dto.getPhuongTien())) {
            List<PhuongTien> phuongTienList = new ArrayList<>();
            dto.getPhuongTien().stream().map(pt -> phuongTienList.add(phuongTienService.getPhuongTien(pt))).collect(Collectors.toList());
            chuyenThamQuan.setPhuongTien(phuongTienList);
        }
        if (!ObjectUtils.isEmpty(dto.getGiangVienDangKy())) {
            chuyenThamQuan.setGiangVienDangKy(giangVienService.getGiangVienByIdCore(dto.getGiangVienDangKy()));

        }
        chuyenThamQuan.setTenChuyenThamQuan(dto.getTenChuyenThamQuan());
        chuyenThamQuan.setThoiGianKhoiHanh(dto.getThoiGianKhoiHanh());
        chuyenThamQuan.setNoiDungThamQuan(dto.getNoiDungThamQuan());
        chuyenThamQuan.setDiaDiemKhoiHanh(diaDiem);
        chuyenThamQuan.setThoiGianBatDauThamQuan(dto.getThoiGianBatDauThamQuan());
        chuyenThamQuan.setThoiGianKetThucThamQuan(dto.getThoiGianKetThucThamQuan());
        chuyenThamQuan.setPhuCapCongTacVien(dto.getPhuCapCongTacVien());
        chuyenThamQuan.setKinhPhi(dto.getKinhPhi());
        chuyenThamQuan.setThueXeNgoaiTruong(dto.isThueXeNgoaiTruong());
        chuyenThamQuan.setTrangThai(EnumTrangThai.DANG_XU_LY.name());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan diemDanh(String id, String maSV) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);

        //nếu chuyến đi chưa được duyệt
        if (!chuyenThamQuan.getTrangThai().equals(EnumTrangThai.SAN_SANG.name())) {
            throw new InvalidException("Chuyến tham quan chưa sẵn sàng!");
        }

        //nếu chưa tới thời gian
        Date now = new Date();
        if (now.before(chuyenThamQuan.getThoiGianKhoiHanh())) {
            throw new InvalidException("Chưa đến thời gian chuyến tham quan!");
        }

        //nếu chuyến đi đã kết thúc
        if (now.after(chuyenThamQuan.getThoiGianKetThucThamQuan())) {
            throw new InvalidException("Chuyến tham quan đã kết thúc!");
        }

        //TODO: turn maSV into sinhVienId
        Optional<SinhVien> sinhVien = Optional.ofNullable(sinhVienRepository.findByMaSV(maSV).orElseThrow(() -> new NotFoundException(String.format("Sinh viên có MSSV %s không có trong danh sách!", maSV))));

        SinhVienThamQuan SVtemp = chuyenThamQuan.getDanhSachSinhViens().stream()
                .filter(sinhVienThamQuan -> sinhVienThamQuan.getSinhVien().getId().equals(sinhVien.get().getId()))
                .findAny()
                .orElse(null);

        //SV not found
        if (SVtemp == null) {
            throw new NotFoundException(String.format("Sinh viên có MSSV %s không có trong danh sách!", maSV));
        }

        //
        if (SVtemp.isTrangThai()) {
            throw new InvalidException(String.format("Sinh viên có MSSV %s đã điểm danh!", maSV));

/*
            SVtemp.setTrangThai(false);
            chuyenThamQuanRepository.save(chuyenThamQuan);
            return chuyenThamQuan;
*/
        }

        //TODO: đi trễ
        Date batDau = chuyenThamQuan.getThoiGianBatDauThamQuan();
        if (now.after(batDau)) {
            SVtemp.setThoiGianDiemDanh(now);
            SVtemp.setSoPhutDiTre((int) calulateTime(now, batDau));
            SVtemp.setTrangThai(true);
            chuyenThamQuanRepository.save(chuyenThamQuan);
            return chuyenThamQuan;
        }

        SVtemp.setThoiGianDiemDanh(now);
        SVtemp.setTrangThai(true);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    private double calulateTime(Date thoiGianDen, Date thoiGianBatDau) {
        long totalTimeInMili = thoiGianDen.getTime() - thoiGianBatDau.getTime();
        double totalTimeInMins = totalTimeInMili / (60 * 1000);
        return totalTimeInMins;
    }

    @Override
        public ChuyenThamQuan ctvCheckInCheckOutAndUploadHinh(String chuyenThamQuanId, String typeCheck, CheckinCheckoutDto checkinCheckoutDto, Principal principal) {
        if (ObjectUtils.isEmpty(checkinCheckoutDto.getFile())) {
            throw new InvalidException("Vui lòng chọn hình ảnh minh chứng cho " + typeCheck.toLowerCase() + "!");
        }
        if (ObjectUtils.isEmpty(chuyenThamQuanId)) {
            throw new InvalidException("Chuyến tham quan id không được bỏ trống!");
        }
        if (ObjectUtils.isEmpty(checkinCheckoutDto.getKinhDo())) {
            throw new InvalidException("Kinh độ không được bỏ trống!");
        }
        if (ObjectUtils.isEmpty(checkinCheckoutDto.getViDo())) {
            throw new InvalidException("Vĩ độ không được bỏ trống!");
        }
        CongTacVien congTacVien = congTacVienService.getCurrent(principal);
        GPS gps = new GPS();
        gps.setKinhDo(checkinCheckoutDto.getKinhDo());
        gps.setViDo(checkinCheckoutDto.getViDo());
        LocalDateTime current = LocalDateTime.now();
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        List<CongTacVienDanDoan> congTacVienDanDoans = chuyenThamQuan.getCongTacViens();

        CongTacVienDanDoan congTacVienDanDoan = congTacVienDanDoans.stream()
                .filter(ctvdd -> ctvdd.getCongTacVien().getId().equals(congTacVien.getId()))
                .findAny()
                .orElseThrow(() -> new InvalidException("Cộng tác viên không dẫn đoàn chuyến này."));

        //trường hợp checkin
        if (typeCheck.trim().equalsIgnoreCase("check-in")) {
            congTacVienDanDoan.setThoiGianDenCongTy(current.toDate());
            MyFile myFile = myFileService.getFileInfo(checkinCheckoutDto.getFile());
            congTacVienDanDoan.setFileCheckIn(myFile.getId());
            congTacVienDanDoan.setGpsCheckIn(gps);
            // trường hợp checkout
        } else {
            congTacVienDanDoan.setThoiGianRoiCongTy(current.toDate());
            MyFile myFile = myFileService.getFileInfo(checkinCheckoutDto.getFile());;
            congTacVienDanDoan.setFileCheckOut(myFile.getId());
            congTacVienDanDoan.setGpsCheckout(gps);
        }
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public void sendMailToDoanhNghiep(String chuyenThamQuanId) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        Map<String, Object> model = new HashMap<>();
        model.put("tenCongTy", chuyenThamQuan.getDoanhNghiep().getCongTy());
        model.put("tenChuyenThamQuan", chuyenThamQuan.getTenChuyenThamQuan());
        model.put("thoiGianBatDauThamQuan", chuyenThamQuan.getThoiGianBatDauThamQuan());
        model.put("thoiGianKetThucThamQuan", chuyenThamQuan.getThoiGianKetThucThamQuan());
        model.put("danhsachGiangVienThamGia", chuyenThamQuan.getDanhSachGiangVienThamGias().size());
        model.put("danhSachSinhVien", chuyenThamQuan.getDanhSachSinhViens().size());
        model.put("congTacVien", chuyenThamQuan.getCongTacViens().size());
        model.put("noiDungThamQuan", chuyenThamQuan.getNoiDungThamQuan());
        model.put("emailGiangVienDangKy", chuyenThamQuan.getGiangVienDangKy().getEmail());
        emailService.sendTextMail(chuyenThamQuan.getDoanhNghiep().getEmail(), "THƯ XÁC NHẬN TỔ CHỨC CHUYẾN THAM QUAN DOANH NGHIỆP", "confirm-tour-organization.ftl", model, "");
    }

    @Override
    public void importDanhSachSinhVien(String chuyenThamQuanId, MultipartFile file) throws Exception {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        excelService.importDanhSachSinhVien(chuyenThamQuan, file);
    }

    @Override
    public File xuatCongVanGuiDoanhNghiep(String chuyenThamQuanId) throws Exception {
        if (ObjectUtils.isEmpty(chuyenThamQuanId)) {
            throw new InvalidException("Chuyến tham quan id không được bỏ trống!");
        }
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        return wordService.xuatCongVanXinThamQuanGuiDoanhNghiep(chuyenThamQuan);
    }

    public ChuyenThamQuan sendMailChuyenThamQuan(String chuyenThamQuanId) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        String gioiTinh = "";
        if (chuyenThamQuan.getGiangVienDangKy().isGioiTinh() == true) {
            gioiTinh = "Cô";
        } else gioiTinh = "Thầy";

        String phuongTienThuoc = "";
        if (chuyenThamQuan.isThueXeNgoaiTruong() == false) {
            phuongTienThuoc = "Xe của trường";
        } else phuongTienThuoc = "Xe thuê ngoài trường";

        List<SinhVien> sinhViens = new ArrayList<SinhVien>();
        chuyenThamQuan.getDanhSachSinhViens().forEach(sinhVienThamQuan -> sinhViens.add(sinhVienThamQuan.getSinhVien()));
        List<GiangVien> giangViens = new ArrayList<GiangVien>();
        chuyenThamQuan.getDanhSachGiangVienThamGias().forEach(idGiangVien -> giangViens.add(giangVienService.getGiangVien(idGiangVien)));


        Map<String, Object> model = new HashMap<>();
        model.put("dotthamquan", chuyenThamQuan.getDotThamQuan().getTenDotThamQuan());
        model.put("chucdanh", chuyenThamQuan.getGiangVienDangKy().getChucDanhKhoaHoc());
        model.put("gioitinh", gioiTinh);
        model.put("tenthamquan", chuyenThamQuan.getTenChuyenThamQuan());
        model.put("thoigiankhoihanh", dateTimeFormat.format(chuyenThamQuan.getThoiGianKhoiHanh()));
        model.put("diadiemkhoihanh", chuyenThamQuan.getDiaDiemKhoiHanh());
        model.put("phuongtien", chuyenThamQuan.getPhuongTien());
        model.put("phuongtienthuoc", phuongTienThuoc);
        model.put("tendoanhnghiep", chuyenThamQuan.getDoanhNghiep().getCongTy());
        model.put("thoigianbatdau", dateTimeFormat.format(chuyenThamQuan.getThoiGianBatDauThamQuan()));
        model.put("thoigianketthuc", dateTimeFormat.format(chuyenThamQuan.getThoiGianKetThucThamQuan()));
        model.put("noidung", chuyenThamQuan.getNoiDungThamQuan());
        model.put("listgiangvien", giangViens);
        model.put("listsinhvien", sinhViens);
        model.put("listcongtacvien", chuyenThamQuan.getCongTacViens());
        emailService.sendTextMail(chuyenThamQuan.getGiangVienDangKy().getEmail(), "Thông Báo Tham Quan Doanh Nghiệp " + chuyenThamQuan.getDotThamQuan().getTenDotThamQuan(), "send-mail-tham-quan-gvdk.ftl", model, "Check Pls");
        return chuyenThamQuan;
    }

    @Override
    public List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndCongTacVien(Principal principal, Boolean trangThai, String trangThaiChuyen) {
        CongTacVien congTacVien = congTacVienService.getCurrent(principal);
        return chuyenThamQuanRepository.getAllChuyenThamQuanByDotActiveAndCongTacVien(congTacVien.getEmail(), trangThai, trangThaiChuyen);
    }

    @Override
    public List<ChuyenThamQuan> getAllChuyenThamQuanByDotVaGiangVienThamGia(String idDotThamQuan, Principal principal) {
        GiangVien giangVien = giangVienService.getCurrentGiangVien(principal.getName());

        if (idDotThamQuan == null) {
            List<ChuyenThamQuan> chuyenThamQuans = new ArrayList<>();
            List<DotThamQuan> dotThamQuans = dotThamQuanService.getAllByTrangThaiTrue();
            for (DotThamQuan dotThamQuan : dotThamQuans) {
                chuyenThamQuanRepository.getAllChuyenThamQuanByDotActiveAndGiangVienThamGia(dotThamQuan.getId(), giangVien.getId()).stream().map(gv -> chuyenThamQuans.add(gv)).collect(Collectors.toList());
            }
            return chuyenThamQuans;
        } else {
            return chuyenThamQuanRepository.getAllChuyenThamQuanByDotActiveAndGiangVienThamGia(idDotThamQuan, giangVien.getId());
        }
    }

    @Override
    public List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(String idDotThamQuan, Principal principal) {
        SinhVien sinhVien = sinhVienService.getCurrentSinhVien(principal);
        if (idDotThamQuan == null) {
            List<ChuyenThamQuan> chuyenThamQuans = new ArrayList<>();
            List<DotThamQuan> dotThamQuans = dotThamQuanService.getAllByTrangThaiTrue();
            for (DotThamQuan dotThamQuan : dotThamQuans) {
                chuyenThamQuanRepository.getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(dotThamQuan.getId(), sinhVien.getId()).stream().map(sv -> chuyenThamQuans.add(sv)).collect(Collectors.toList());
            }
            return chuyenThamQuans;

        } else {
            return chuyenThamQuanRepository.getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(idDotThamQuan, sinhVien.getId());
        }
    }

    @Override
    public List<Map> getChuyenThamQuansByThueXeNgoaiTruong(String dotThamQuanId) {
        List<ChuyenThamQuan> chuyenThamQuans = chuyenThamQuanRepository.getChuyenThamQuansByThueXeNgoaiTruong(dotThamQuanId);
        List<Map> ctq = new ArrayList<>();
        int stt = 1;
        for (int i = 0; i < chuyenThamQuans.size(); i++) {
            Map chuyenThamQuanThongKe = new HashMap<>();

            chuyenThamQuanThongKe.put("STT", stt);
            chuyenThamQuanThongKe.put("Ngay", dateFormat.format(chuyenThamQuans.get(i).getThoiGianKhoiHanh()));
            chuyenThamQuanThongKe.put("So sinh vien", chuyenThamQuans.get(i).getDanhSachSinhViens().size());

            //TODO: xử lý phuong tiện từ string sang array
            List<Integer> soLuongPhuongTien = thongKeSoLuongPhuongTien(chuyenThamQuans.get(i).getPhuongTien());
            if (soLuongPhuongTien.get(0) == 0) {
                chuyenThamQuanThongKe.put("Xe 16 cho", "");
            } else {
                chuyenThamQuanThongKe.put("Xe 16 cho", soLuongPhuongTien.get(0));
            }
            if (soLuongPhuongTien.get(1) == 0) {
                chuyenThamQuanThongKe.put("Xe 29 cho", "");
            } else {
                chuyenThamQuanThongKe.put("Xe 29 cho", soLuongPhuongTien.get(1));
            }
            if (soLuongPhuongTien.get(2) == 0) {
                chuyenThamQuanThongKe.put("Xe 45 cho", "");
            } else {
                chuyenThamQuanThongKe.put("Xe 45 cho", soLuongPhuongTien.get(2));
            }
            chuyenThamQuanThongKe.put("Noi den", chuyenThamQuans.get(i).getDoanhNghiep().getCongTy() + "\n" + chuyenThamQuans.get(i).getDoanhNghiep().getDiaChi());
            chuyenThamQuanThongKe.put("Thoi gian", timeFormat.format(chuyenThamQuans.get(i).getThoiGianBatDauThamQuan()) + " - " + timeFormat.format(chuyenThamQuans.get(i).getThoiGianKetThucThamQuan()));
            stt++;
            ctq.add(chuyenThamQuanThongKe);
        }
        return ctq;
    }

    @Override
    public File exportDanhSachChuyenThamQuanSuDungXeNgoai(String dotThamQuanId) throws Exception {
        List<Map> chuyenThamQuans = getChuyenThamQuansByThueXeNgoaiTruong(dotThamQuanId);
        return excelService.exportDanhSachChuyenThamQuanSuDungXeNgoai(chuyenThamQuans);
    }

    @Override
    public List<ChuyenThamQuanTinhLuongDto> getDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(String dotThamQuanId) {

        //get dotThamQuan
        DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(dotThamQuanId);

        if (dotThamQuan == null) {
            throw new NotFoundException(String.format("Đợt tham quan có id %s không tồn tại", dotThamQuanId));
        }

        //get all chuyenThamQuan
        List<ChuyenThamQuan> chuyenThamQuans = chuyenThamQuanRepository.getChuyenThamQuansCoCTVByDotThamQuan(dotThamQuan.getId());

        List<ChuyenThamQuanTinhLuongDto> ctq = new ArrayList<>();
        for (int i = 0; i < chuyenThamQuans.size(); i++) {
            ChuyenThamQuanTinhLuongDto dto = new ChuyenThamQuanTinhLuongDto();

            // dto.setCongTacViens(chuyenThamQuans.get(i).getCongTacViens());
            dto.setCongTy(chuyenThamQuans.get(i).getDoanhNghiep());
            dto.setNgayThamQuan(chuyenThamQuans.get(i).getThoiGianKhoiHanh());

            Date batDau = chuyenThamQuans.get(i).getThoiGianBatDauThamQuan();
            Date ketThuc = chuyenThamQuans.get(i).getThoiGianKetThucThamQuan();
            double time = calulateTime(ketThuc, batDau) / 60;
            BigDecimal formatTime = new BigDecimal(time).setScale(2, RoundingMode.HALF_DOWN);

            dto.setThoiGianBatDauThamQuan(batDau);
            dto.setThoiGianKetThucThamQuan(ketThuc);
            dto.setTongSoGio(formatTime.doubleValue());

            double donGia = chuyenThamQuans.get(i).getPhuCapCongTacVien();
            if (donGia == 0) {
                donGia = cauHinhHeThongService.getPhuCapCTV();
            }

            dto.setDonGia(donGia);
            dto.setThanhTien(donGia * formatTime.doubleValue());
//            dto.setKyNhan(chuyenThamQuans.get(i).getCongTacViens());

            ctq.add(dto);
        }
        return ctq;
    }

    @Override
    public File exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(String dotThamQuanId) throws Exception {
        DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(dotThamQuanId);
        if (dotThamQuan == null) {
            throw new NotFoundException(String.format("Đợt tham quan có id %s không tồn tại", dotThamQuanId));
        }
        List<ChuyenThamQuanTinhLuongDto> chuyenThamQuans = getDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(dotThamQuan.getId());
        return excelService.exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(chuyenThamQuans, dotThamQuan.getTenDotThamQuan());
    }

    @Override
    public ChuyenThamQuan congTacVienCheckInCheckOut(String id, Boolean check) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        if (check == true) {
            chuyenThamQuan.setThoiGianBatDauThamQuan(new Date());
        } else {
            chuyenThamQuan.setThoiGianKetThucThamQuan(new Date());
        }
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan getChuyenThamQuanByThoiGianKhoiHanhAndCongTacVien(Date thoiGianKhoiHanh, String email) {
        ChuyenThamQuan chuyenThamQuan = chuyenThamQuanRepository.getChuyenThamQuanByThoiGianKhoiHanhAndCongTacVien(thoiGianKhoiHanh, email);
        return chuyenThamQuan;
    }

    @Override
    public File xuatDanhSachSinhVienThamGiaThamQuan(String chuyenThamQuanId) throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        if (chuyenThamQuan == null) {
            throw new NotFoundException("Khong tim thay chuyen tham quan");
        }
        return excelService.xuatDanhSachSinhVienThamQuan(chuyenThamQuan);
    }

    @Override
    public File xuatDanhSachSinhVienVang(String chuyenThamQuanId) throws Exception {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        if (chuyenThamQuan == null) {
            throw new NotFoundException(String.format("Chuyến tham quan có id %s không tồn tại", chuyenThamQuanId));
        }
        return excelService.xuatDanhSachSinhVienVang(chuyenThamQuan);
    }

    @Override
    public String sendCongVanDenCongTyThamQuan(String chuyenThamQuanId) throws Exception {
        if (ObjectUtils.isEmpty(chuyenThamQuanId)) {
            throw new InvalidException("Chuyến tham quan id không được bỏ trống!");
        }
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);

        Map<String, Object> model = new HashMap<>();
        model.put("tenCongTy", chuyenThamQuan.getDoanhNghiep().getCongTy());
        model.put("emailGiangVienPhuTrach", chuyenThamQuan.getGiangVienDangKy().getEmail());

        File file = xuatCongVanGuiDoanhNghiep(chuyenThamQuanId).getAbsoluteFile();
        if (file == null) {
            throw new NotFoundException(String.format("Không tồn tại file!"));
        }

        emailService.sendFileMail(chuyenThamQuan.getDoanhNghiep().getEmail(), "CÔNG VĂN ĐỀ NGHỊ THAM QUAN DOANH NGHIỆP", "mau-cv-xin-tham-quan.ftl", model, "", Collections.singletonList(file));
        return "Gửi email đến doanh nghiệp thành công!";
    }

    @Override
    public Page<ChuyenThamQuan> getChuyenThamQuanPagingChuaCoCongTacVien(String search, int page, int size, String sort, String column, String idDotThamQuan) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return chuyenThamQuanRepository.getChuyenThamQuanPagingChuaCoCongTacVien(idDotThamQuan, vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public File xuatKeHoachThamQuan(String idDotThamQuan) throws Exception {
        DotThamQuan dotThamQuan = dotThamQuanService.getDotThamQuanByIdCore(idDotThamQuan);
        List<ChuyenThamQuan> chuyenThamQuans = chuyenThamQuanRepository.getChuyenThamQuansByDotThamQuan(dotThamQuan.getId());
        if (chuyenThamQuans.size() == 0)
            throw new InvalidException(String.format("Đợt tham quan với id %s hiện tại không có chuyến tham quan nào", dotThamQuan.getId()));
        return wordService.xuatKeHoachThamQuan(dotThamQuan, chuyenThamQuans);
    }

    public String guiMailTuDong(String id) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        if (!chuyenThamQuan.getTrangThai().equals(EnumTrangThai.SAN_SANG.name())) {
            throw new InvalidException("Chuyến tham quan chưa sẵn sàng!");
        }

        Map<String, Object> model = new HashMap<>();
        List<String> mailDangKyThamGias = new ArrayList<>();
        int gvSize = chuyenThamQuan.getDanhSachGiangVienThamGias().size();
        if (gvSize == 0 && chuyenThamQuan.getCongTacViens().size() == 0) {
            throw new InvalidException("Chuyến tham quan chưa có giảng viên và cộng tác viên tham gia");
        }

        // giang vien
        if (gvSize != 0) {
            List<GiangVien> giangViens = chuyenThamQuan.getDanhSachGiangVienThamGias().stream().map(gv -> giangVienService.getGiangVien(gv)).collect(Collectors.toList());
            List<String> giangVienThamGia = giangViens.stream().map(gv -> {
                mailDangKyThamGias.add(gv.getEmail());
                String hoTen_SDT = "";
                hoTen_SDT = gv.isGioiTinh() ? "Cô " : "Thầy ";
                hoTen_SDT += gv.getHoTen() + " - " + gv.getDienThoai();
                return hoTen_SDT;
            }).collect(Collectors.toList());
            model.put("giangVienThamGias", giangVienThamGia);
        }

        // cong tac vien
        if (chuyenThamQuan.getCongTacViens().size() == 0) {
            throw new InvalidException("Chuyến tham quan chưa có cộng tác viên.");
        }
        List<String> congTacViens = new ArrayList<>();
        chuyenThamQuan.getCongTacViens().forEach(ctv -> {
            if (ctv.getTrangThai().equals(true)) {
                CongTacVien congTacVien = ctv.getCongTacVien();
                mailDangKyThamGias.add(congTacVien.getEmail());
                String hoTen_SDT = congTacVien.getHoTen() + " - " + congTacVien.getDienThoai();
                congTacViens.add(hoTen_SDT);
            }
        });

        // phuong tien
        List<Integer> soLuongPhuongTien = thongKeSoLuongPhuongTien(chuyenThamQuan.getPhuongTien());
        List<Integer> soXe = Arrays.asList(16, 29, 45);
        String phuongTien = "";
        for (int i = 0; i < soLuongPhuongTien.size(); i++) {
            phuongTien = !soLuongPhuongTien.get(i).equals(0) ? phuongTien + soLuongPhuongTien.get(i) + " xe " + soXe.get(i) + " chỗ " : phuongTien;
        }

        mailDangKyThamGias.add(chuyenThamQuan.getGiangVienDangKy().getEmail());
        List<SinhVien> sinhViens = chuyenThamQuan.getDanhSachSinhViens().stream().map(svtq -> svtq.getSinhVien()).collect(Collectors.toList());
        sinhViens.stream().map(sv -> mailDangKyThamGias.add(sv.getEmail()));


        model.put("TENCONGTY", chuyenThamQuan.getDoanhNghiep().getCongTy());
        model.put("thoiGianKhoiHanh", chuyenThamQuan.getThoiGianKhoiHanh());
        model.put("diaDiemKhoiHanh", chuyenThamQuan.getDiaDiemKhoiHanh().getTenDiaDiem());
        model.put("diaChiDoanhNghiep", chuyenThamQuan.getDoanhNghiep().getDiaChi());
        model.put("gvSize", gvSize);
        model.put("phuongTien", phuongTien);
        model.put("congTacViens", congTacViens);

        try {
            Template template = freemarkerConfig.getTemplate("mail-nhac-tham-quan-tu-dong.ftl");
            freemarkerConfig.setDefaultEncoding("UTF-8");
            freemarkerConfig.setLocale(Locale.US);
            String messageMail = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            TextEmail textEmail = new TextEmail();
            textEmail.setClientId(cliendId);
            textEmail.setEmailSender("dev1.stc@hcmute.edu.vn");
            textEmail.setPassWordSender("stc-dev@123456");
            for (int i = 0; i < mailDangKyThamGias.size(); i++) {
                textEmail.setSendTos(Collections.singletonList(mailDangKyThamGias.get(i)));
            }
            textEmail.setSubject("Nhắc nhở về việc Đăng ký tham gia chuyến tham quan doanh nghiệp");
            textEmail.setMessage(messageMail);
            Date thoiGianKhoiHanh = chuyenThamQuan.getThoiGianKhoiHanh();
            Date thoiGianGuiMail = new LocalDateTime(thoiGianKhoiHanh).minusDays(1).toDate();
            EmailDto dto = new EmailDto(thoiGianGuiMail, "Nhắc nhở về việc Đăng ký tham gia chuyến tham quan doanh nghiệp", textEmail, null);
            scheduleServiceClient.createEmailSchedule(dto);
        } catch (Exception e) {
            throw new InvalidException(String.format("Error: %s", e.getMessage()));
        }
        return "Gửi thành công";
    }

    @Override
    public String diemDanhBu(String chuyenThamQuanId, String maSV, int soPhutDiTre) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        Optional<SinhVien> sinhVien = sinhVienRepository.findByMaSV(maSV);
        SinhVienThamQuan sinhVienThamQuan = chuyenThamQuan.getDanhSachSinhViens().stream()
                .filter(svtq -> svtq.getSinhVien().getId().equals(sinhVien.get().getId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Sinh viên có MSSV %s không có trong danh sách!", maSV)));
        sinhVienThamQuan.setThoiGianDiemDanh(new LocalDateTime().toDate());
        sinhVienThamQuan.setSoPhutDiTre(soPhutDiTre);
        sinhVienThamQuan.setTrangThai(true);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return "Điểm danh bù thành công";
    }

    @Override
    public ChuyenThamQuan importDanhSachSinhVienDiemDanhBu(String chuyenThamQuanId, MultipartFile file) throws Exception {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        excelService.importDanhSachSinhVienDiemDanhBu(chuyenThamQuan, file);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan congTacVienDangKyDanDoan(String idChuyenThamQuan, Principal principal, boolean huy) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(idChuyenThamQuan);
        List<CongTacVienDanDoan> congTacViens = chuyenThamQuan.getCongTacViens();
        CongTacVien congTacVien = congTacVienService.getCurrent(principal);

        if (huy == false) {
            if (!chuyenThamQuan.getTrangThai().equals(EnumTrangThai.DANG_XU_LY.name())) {
                throw new InvalidException("Trạng thái chuyến tham quan không thể thêm cộng tác viên");
            }
            ChuyenThamQuan chuyenThamQuanDaCo = getChuyenThamQuanByThoiGianKhoiHanhAndCongTacVien(chuyenThamQuan.getThoiGianKhoiHanh(), principal.getName());
            if (!ObjectUtils.isEmpty(chuyenThamQuanDaCo)) {
                throw new InvalidException("Thời gian khởi hành trùng với chuyến tham quan đã đăng ký");
            }
            for (CongTacVienDanDoan ctv : congTacViens) {
                if (ctv.getCongTacVien().getId().equals(congTacVien.getId())) {
                    throw new InvalidException("Cộng tác viên đã đăng ký chuyến tham quan này");
                }
            }
            CongTacVienDanDoan congTacVienDanDoan = new CongTacVienDanDoan(congTacVien, congTacVien);
            congTacViens.add(congTacVienDanDoan);
        } else {
            int pos = -1;
            for (CongTacVienDanDoan ctv : congTacViens) {
                if (ctv.getCongTacVien().getId().equals(congTacVien.getId())) {
                    if (ctv.getTrangThai() == null) {
                        pos = congTacViens.indexOf(ctv);
                    } else {
                        throw new InvalidException("Admin xác nhận, không thể hủy.");
                    }
                }
            }
            if (pos >= 0) {
                congTacViens.remove(congTacViens.get(pos));
            }
        }
        chuyenThamQuan.setCongTacViens(congTacViens);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;

    }

    @Override
    public ChuyenThamQuan updateTrangThaiChuyenThamQuan(String id, String trangThai) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        chuyenThamQuan.setTrangThai(trangThai);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public String guiMailCapNhatTrangThai(String id, String lyDo) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        Integer isLyDo = 0;
        if (!lyDo.equals("")) {
            isLyDo = 1;
            chuyenThamQuan.setLyDo(lyDo);
        }
        if (chuyenThamQuan.getTrangThai().equals(EnumTrangThai.HUY.name()) && isLyDo == 0) {
            throw new InvalidException("Vui lòng điền lý do khi hủy chuyến tham quan");
        }
        Map<String, Object> model = new HashMap<>();
        String gioiTinh = chuyenThamQuan.getGiangVienDangKy().isGioiTinh() ? "Cô" : "Thầy";
        model.put("tenCTQ", chuyenThamQuan.getTenChuyenThamQuan());
        model.put("gioitinh", gioiTinh);
        model.put("chucdanh", chuyenThamQuan.getGiangVienDangKy().getChucDanhKhoaHoc());
        model.put("trangthai", chuyenThamQuan.getTrangThai());
        model.put("isLyDo", isLyDo);
        model.put("lydo", lyDo);
        emailService.sendTextMail(chuyenThamQuan.getGiangVienDangKy().getEmail(), "THÔNG BÁO VỀ VIỆC CẬP NHẬT TRẠNG THÁI CHUYẾN THAM QUAN", "thong-bao-cap-nhat-trang-thai.ftl", model, "");
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return "Gửi thành công đến giảng viên đăng ký";
    }

    @Override
    public List<ChuyenThamQuan> getChuyenThamQuanActiveByTrangThai() {
        return chuyenThamQuanRepository.getChuyenThamQuanActiveByTrangThai();
    }

    @Override
    public List<ChuyenThamQuan> getDuLieuTimeLineGraphic(String trangThai, Date tuNgay, Date denNgay, String
            idDotThamQuan) {
        if (idDotThamQuan == null) {
            List<ChuyenThamQuan> chuyenThamQuanList = new ArrayList<>();
            List<DotThamQuan> dotThamQuans = dotThamQuanService.getAllByTrangThaiTrue();
            for (int i = 0; i < dotThamQuans.size(); i++) {
                List<ChuyenThamQuan> chuyenThamQuans = chuyenThamQuanRepository.getDuLieuTimeLineGraphic(trangThai, tuNgay, denNgay, dotThamQuans.get(i).getId());
                chuyenThamQuans.stream().map(ctq -> chuyenThamQuanList.add(ctq)).collect(Collectors.toList());
            }
            return chuyenThamQuanList;

        } else {
            return chuyenThamQuanRepository.getDuLieuTimeLineGraphic(trangThai, tuNgay, denNgay, idDotThamQuan);
        }
    }

    @Override
    public File xuatPhieuXacNhanSauThamQuan(String chuyenThamQuanId) throws Exception {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
        return wordService.xuatPhieuXacNhanThamQuan(chuyenThamQuan);
    }

    @Override
    public ChuyenThamQuan luuTruHoSoChuyenThamQuan(String id, LuuTruHoSoDto dto) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        if (ObjectUtils.isEmpty(dto.getFileKeHoach()) || ObjectUtils.isEmpty(dto.getFileXacNhan())) {
            throw new InvalidException("File truyền vào không được để trống");
        }
        if (ObjectUtils.isEmpty(dto.getDuongDan())) {
            throw new InvalidException("Đường dẫn lưu trữ hình ảnh chuyến tham quan không được để trống");
        }
        MyFile myFileKeHoach = myFileService.getFileInfo(dto.getFileKeHoach());
        MyFile myFileXacNhan = myFileService.getFileInfo(dto.getFileXacNhan());
        chuyenThamQuan.setFileScanKeHoach(myFileKeHoach.getId());
        chuyenThamQuan.setDuongDan(dto.getDuongDan());
        chuyenThamQuan.setFileScanGiayXacNhanDoanhNghiep(myFileXacNhan.getId());
        chuyenThamQuan.setTrangThai(EnumTrangThai.HOAN_TAT.name());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public Page<SinhVienThamQuan> getAllSinhVienThamQuansByChuyenThamQuan(String id, String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, 10, sort, column);
        UnwindOperation unwindOperation = unwind("$danhSachSinhViens");
        ProjectionOperation projectionOperation = Aggregation.project("danhSachSinhViens");

        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        criteria.and("_id").is(new ObjectId(id));

        if (!ObjectUtils.isEmpty(search)) {
            searchCriterias.add(Criteria.where("danhSachSinhViens.sinhVien.maSV").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("danhSachSinhViens.sinhVien.hoTen").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("danhSachSinhViens.sinhVien.lop").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
            searchCriterias.add(Criteria.where("danhSachSinhViens.sinhVien.dienThoai").regex(vietnameseStringUtils.makeSearchRegex(search), "i"));
        }

        if (searchCriterias.size() > 0) {
            criteria.orOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));
        }

        MatchOperation matchOperation = match(criteria);
        SkipOperation skipOperation = new SkipOperation((long) pageable.getPageNumber() * pageable.getPageSize());
        SortOperation sortOperation = sort(pageable.getSort());
        FacetOperation facetOperation = facet(
                count().as("total")).as("total")
                .and(skipOperation, limit(pageable.getPageSize())
                ).as("metaData");
        Aggregation aggregation = newAggregation(
                unwindOperation,
                projectionOperation,
                matchOperation,
                replaceRoot("$danhSachSinhViens"),
                sortOperation,
                facetOperation
        );
        AggregationResults<PageSinhVienThamQuanTransfer> aggregate = mongoTemplate.aggregate(aggregation,
                ChuyenThamQuan.class,
                PageSinhVienThamQuanTransfer.class);
        List<PageSinhVienThamQuanTransfer> mappedResults = aggregate.getMappedResults();
        if (mappedResults.get(0).getTotal().size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        return new PageImpl<>(mappedResults.get(0).getMetaData(), pageable, mappedResults.get(0).getTotal().get(0).getTotal());
    }

    @Override
    public ChuyenThamQuan editListSinhViens(String id, List<String> danhSachSinhVien) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        List<SinhVienThamQuan> sinhVienThamQuans = new ArrayList<>();
        danhSachSinhVien.forEach(idSV -> {
            SinhVien sinhVien = sinhVienService.getSinhVienByIdCore(idSV);
            if (sinhVien != null) {
                SinhVienThamQuan sinhVienThamQuan = sinhVienThamQuans.stream()
                        .filter(svtq -> svtq.getSinhVien().getId().equals(idSV))
                        .findAny()
                        .orElse(null);
                if (!sinhVienThamQuans.contains(sinhVienThamQuan)) {
                    sinhVienThamQuans.add(new SinhVienThamQuan(sinhVien));
                }
            }
        });
        chuyenThamQuan.setDanhSachSinhViens(sinhVienThamQuans);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan deleteSinhVienThamQuan(String id, String idSV) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        for (int i = 0; i < chuyenThamQuan.getDanhSachSinhViens().size(); i++) {
            SinhVienThamQuan sinhVienThamQuan = chuyenThamQuan.getDanhSachSinhViens().get(i);
            if (sinhVienThamQuan.getSinhVien().getId().equals(idSV)) {
                chuyenThamQuan.getDanhSachSinhViens().remove(sinhVienThamQuan);
            }
        }
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public List<ChuyenThamQuan> getChuyenThamQuansByDotThamQuan(String idDotThamQuan) {
        return chuyenThamQuanRepository.getChuyenThamQuansByDotThamQuan(idDotThamQuan);
    }

    @Override
    public ChuyenThamQuan importFileDuyetTuBGH(String id, MultipartFile file, Principal principal) throws Exception {
        if (file.isEmpty()) {
            throw new InvalidException("File không được bỏ trống");
        }
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        MyFile myFile = myFileService.uploadFile(file, "file-pdf-bgh-duyet", principal);
        chuyenThamQuan.setFileXacNhanDuyetTuBanGiamHieu(myFile.getId());
        chuyenThamQuan.setTrangThai(EnumTrangThai.SAN_SANG.name());
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;

    }

    @Override
    public List<ChuyenThamQuan> createDotThamQuanChoChuyen(RequestChuyenThamQuanDto dto) {
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getTenDotThamQuan())) {
            throw new InvalidException("Tên đợt tham quan không được bỏ trống.");
        }
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getMucDich())) {
            throw new InvalidException("Mục đích tham quan không được bỏ trống.");
        }
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getYeuCau())) {
            throw new InvalidException("Yêu cầu tham quan không được bỏ trống.");
        }
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getNoiDungThamQuan())) {
            throw new InvalidException("Nội dung tham quan không được bỏ trống.");
        }
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getTuNgay())) {
            throw new InvalidException("Ngày đi không được bỏ trống.");
        }
        if (ObjectUtils.isEmpty(dto.getDotThamQuan().getDenNgay())) {
            throw new InvalidException("Ngày về không được bỏ trống.");
        }
        if (dto.getDotThamQuan().getTuNgay().after(dto.getDotThamQuan().getDenNgay())) {
            throw new InvalidException("Vui lòng xem lại thời gian đi và thời gian về.");
        }

        DotThamQuan dotThamQuan = dotThamQuanService.createDotThamQuan(dto.getDotThamQuan());
        List<ChuyenThamQuan> chuyenThamQuans = new ArrayList<>();
        dto.getChuyenThamQuans().forEach(chuyenThamQuanId -> {
            ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(chuyenThamQuanId);
            chuyenThamQuan.setDotThamQuan(dotThamQuan);
            chuyenThamQuanRepository.save(chuyenThamQuan);
            chuyenThamQuans.add(chuyenThamQuan);
        });
        return chuyenThamQuans;
    }

    @Override
    public List<ChuyenThamQuan> deleteChuyenThuocDot(String DotThamQuanId, List<String> chuyenThamQuans) {
        List<ChuyenThamQuan> chuyenThamQuanList = chuyenThamQuans.stream().map(ctq -> getChuyenThamQuan(ctq)).collect(Collectors.toList());
        List<String> errors = new ArrayList<>();
        chuyenThamQuanList.forEach(ctq -> {
            if (ObjectUtils.isEmpty(ctq.getDotThamQuan()) || !ctq.getDotThamQuan().getId().equals(DotThamQuanId)) {
                errors.add(String.format("Chuyến tham quan có id %s không thuộc đợt này", ctq.getId()));
            } else {
                ctq.setDotThamQuan(null);
                chuyenThamQuanRepository.save(ctq);
            }
        });
        if (!errors.isEmpty()) {
            throw new InvalidException(errors);
        }
        return chuyenThamQuanList;
    }

    @Override
    public List<ChuyenThamQuan> updateChuyenVaoDot(String DotThamQuanId, List<String> chuyenThamQuans) {
        List<ChuyenThamQuan> chuyenThamQuanList = chuyenThamQuans.stream().map(ctq -> getChuyenThamQuan(ctq)).collect(Collectors.toList());
        chuyenThamQuanList.forEach(ctq -> {
            if (ObjectUtils.isEmpty(ctq.getDotThamQuan()) || !ctq.getDotThamQuan().getId().equals(DotThamQuanId)) {
                ctq.setDotThamQuan(dotThamQuanService.getDotThamQuan(DotThamQuanId));
                chuyenThamQuanRepository.save(ctq);
            }
        });


        return chuyenThamQuanList;
    }

    @Override
    public ChuyenThamQuan adminDuyetCongTacVienDanDoan(String id, String congTacVienId, Boolean duyet, Principal principal) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        CongTacVienDanDoan congTacVienThamQuan = chuyenThamQuan.getCongTacViens().stream()
                .filter(ctv -> ctv.getCongTacVien().getId().equals(congTacVienId))
                .findAny()
                .orElse(null);
        if (congTacVienThamQuan == null) {
            throw new InvalidException("Cộng tác viên chưa đăng ký dẫn đoàn.");
        }
        congTacVienThamQuan.setNgayDuyet(new LocalDateTime().toDate());
        congTacVienThamQuan.setNguoiDuyet(taiKhoanService.getTaiKhoanByEmail(principal.getName()));
        congTacVienThamQuan.setTrangThai(duyet);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    @Override
    public ChuyenThamQuan editListCongTacViens(String id, List<String> danhSachCongTacVien, Principal principal) {
        ChuyenThamQuan chuyenThamQuan = getChuyenThamQuan(id);
        TaiKhoan taiKhoan = taiKhoanService.getCurrent(principal);
        List<CongTacVienDanDoan> congTacVienDanDoans = new ArrayList<>();
        danhSachCongTacVien.forEach(idCTV -> {
            CongTacVien congTacVien = congTacVienService.getCongTacVienByIdCore(idCTV);
            if (congTacVien != null) {
                CongTacVienDanDoan congTacVienDanDoan1 = congTacVienDanDoans.stream()
                        .filter(ctvdd -> ctvdd.getCongTacVien().getId().equals(idCTV))
                        .findAny()
                        .orElse(null);
                if (!congTacVienDanDoans.contains(congTacVienDanDoan1)) {
                    CongTacVienDanDoan congTacVienDanDoan = new CongTacVienDanDoan();
                    congTacVienDanDoan.setCongTacVien(congTacVien);
                    congTacVienDanDoan.setNgayDuyet(new LocalDateTime().toDate());
                    congTacVienDanDoan.setNguoiDuyet(taiKhoan);
                    congTacVienDanDoan.setTrangThai(true);
                    congTacVienDanDoans.add(congTacVienDanDoan);
                }
            }
        });
        chuyenThamQuan.setCongTacViens(congTacVienDanDoans);
        chuyenThamQuanRepository.save(chuyenThamQuan);
        return chuyenThamQuan;
    }

    private List<Integer> thongKeSoLuongPhuongTien(List<PhuongTien> phuongTiens) {
        List<Integer> soLuongPhuongTien = Arrays.asList(0, 0, 0);
        int xe16cho = 0;
        int xe29cho = 0;
        for (int j = 0; j < phuongTiens.size(); j++) {
            PhuongTien pt = phuongTiens.get(j);
            if (pt.getSoChoNgoi() == 16) {
                xe16cho++;
            } else if (pt.getSoChoNgoi() == 29) {
                xe29cho++;
            }
        }
        soLuongPhuongTien.set(0, xe16cho);
        soLuongPhuongTien.set(1, xe29cho);
        soLuongPhuongTien.set(2, phuongTiens.size() - xe16cho - xe29cho);
        return soLuongPhuongTien;
    }
}
