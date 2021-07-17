package com.stc.thamquan.services.excel;

import com.aspose.cells.*;
import com.aspose.cells.DateTime;
import com.stc.thamquan.dtos.chuyenthamquan.ChuyenThamQuanTinhLuongDto;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.repositories.*;
import com.stc.thamquan.utils.EnumRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.joda.time.*;
import org.joda.time.format.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.rmi.ServerException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.stc.thamquan.services.sinhvien.SinhVienService;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:48
 * Filename  : ExcelServiceImpl
 */
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    private final ChuyenThamQuanRepository chuyenThamQuanRepository;

    private final SinhVienRepository sinhVienRepository;

    private final NganhReposiroty nganhReposiroty;

    private final DoanhNghiepRepository doanhNghiepRepository;

    private final LinhVucRepository linhVucRepository;

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final SinhVienService sinhVienService;

    @Value("${file.download_dir}")
    private String download;

    public ExcelServiceImpl(ChuyenThamQuanRepository chuyenThamQuanRepository, SinhVienRepository sinhVienRepository,
                            NganhReposiroty nganhReposiroty, DoanhNghiepRepository doanhNghiepRepository, LinhVucRepository linhVucRepository, SinhVienService sinhVienService) {
        this.chuyenThamQuanRepository = chuyenThamQuanRepository;
        this.sinhVienRepository = sinhVienRepository;
        this.nganhReposiroty = nganhReposiroty;
        this.doanhNghiepRepository = doanhNghiepRepository;
        this.linhVucRepository = linhVucRepository;
        this.sinhVienService = sinhVienService;
    }

    @Override
    public void importDanhSachSinhVien(ChuyenThamQuan chuyenThamQuan, MultipartFile file) throws Exception {
        InputStream stream = new BufferedInputStream(file.getInputStream());
        Workbook book = new Workbook(stream);
        Worksheet sheet = book.getWorksheets().get(0);
        Cells cells = sheet.getCells();
        Range displayRange = cells.getMaxDisplayRange();
        List<SinhVien> sinhViens = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (int row = displayRange.getFirstRow() + 1; row < displayRange.getRowCount(); row++) {
            String maSV = displayRange.get(row, 1).getStringValue();
            String hoTenSV = displayRange.get(row, 3).getStringValue().trim() + " "
                    + displayRange.get(row, 4).getStringValue().trim();
            String dienThoaiSV = displayRange.get(row, 5).getStringValue();
            Date ngaySinhSV = dateFormat.parse(displayRange.get(row, 6).getStringValue());
            String lopSV = displayRange.get(row, 7).getStringValue();
            String maNganhSV = displayRange.get(row, 9).getStringValue();
            String tenNganhSV = displayRange.get(row, 10).getStringValue();

            if (!ObjectUtils.isEmpty(maSV)
                    && !ObjectUtils.isEmpty(hoTenSV)
                    && !ObjectUtils.isEmpty(dienThoaiSV)
                    && !ObjectUtils.isEmpty(ngaySinhSV)
                    && !ObjectUtils.isEmpty(lopSV)
                    && !ObjectUtils.isEmpty(maNganhSV)
                    && !ObjectUtils.isEmpty(tenNganhSV)) {
                SinhVien sinhVien = sinhVienRepository.findByMaSV(maSV).orElse(null);
                if (sinhVien == null) {
                    Nganh nganh = nganhReposiroty.findByMaNganh(maNganhSV).orElse(null);
                    if (nganh != null) {
                        //TODO: tạo mới sinh viên
                        sinhVien = new SinhVien();
                        sinhVien.setMaSV(maSV);
                        sinhVien.setHoTen(hoTenSV);
                        sinhVien.setDienThoai(dienThoaiSV);
                        sinhVien.setNgaySinh(ngaySinhSV);
                        sinhVien.setLop(lopSV);
                        sinhVien.setNganh(nganh);
                        sinhVien.setRoles(Collections.singletonList(EnumRole.ROLE_SINH_VIEN.name()));
                        sinhVienRepository.save(sinhVien);
                        sinhViens.add(sinhVien);
                    } else {
                        errors.add(String.format("Mã ngành dòng %s không tồn tại.", row));
                    }
                } else {
                    sinhViens.add(sinhVien);
                }
            } else {
                errors.add(String.format("Dữ liệu dòng %s không được bỏ trống.", row));
            }

        }
        chuyenThamQuan.setDanhSachSinhViens(sinhViens.stream().map(sinhVien -> new SinhVienThamQuan(sinhVien))
                .collect(Collectors.toList()));
        chuyenThamQuanRepository.save(chuyenThamQuan);
        if (!errors.isEmpty()) {
            throw new InvalidException(errors);
        }

    }


    @Override
    public void importDanhSachDoanhNghieps(MultipartFile file) throws Exception {
        InputStream stream = new BufferedInputStream(file.getInputStream());
        Workbook book = new Workbook(stream);
        Worksheet sheet = book.getWorksheets().get(0);
        Cells cells = sheet.getCells();
        Range displayRange = cells.getMaxDisplayRange();
        List<DoanhNghiep> doanhNghieps = new ArrayList<>();

        List<String> errors = new ArrayList<>();

        for (int row = displayRange.getFirstRow() + 1; row < displayRange.getRowCount(); row++) {
            String hoTenDN = displayRange.get(row, 1).getStringValue();
            String emailDN = displayRange.get(row, 2).getStringValue();
            Boolean gioiTinhDN = displayRange.get(row, 3).getBoolValue();
            String dienThoaiDN = displayRange.get(row, 4).getStringValue();
            String congTyDN = displayRange.get(row, 5).getStringValue();
            String diaChiDN = displayRange.get(row, 6).getStringValue();
            String maSoThueDN = displayRange.get(row, 7).getStringValue();
            String maLinhVucDN = displayRange.get(row, 8).getStringValue();
            String linhVucDN = displayRange.get(row, 9).getStringValue();

            if (!ObjectUtils.isEmpty(emailDN)
                    && !ObjectUtils.isEmpty(hoTenDN)
                    && !ObjectUtils.isEmpty(gioiTinhDN)
                    && !ObjectUtils.isEmpty(dienThoaiDN)
                    && !ObjectUtils.isEmpty(congTyDN)
                    && !ObjectUtils.isEmpty(diaChiDN)
                    && !ObjectUtils.isEmpty(maSoThueDN)
                    && !ObjectUtils.isEmpty(maLinhVucDN)
                    && !ObjectUtils.isEmpty(linhVucDN)
            ) {
                if (!doanhNghiepRepository.existsByEmailIgnoreCase(emailDN)) {
                    if (!doanhNghiepRepository.existsByMaSoThueIgnoreCase(maSoThueDN)) {
                        //TODO: xử lý lĩnh vực từ string sang array
                        List<String> maLinhVucs = new ArrayList(Arrays.asList(maLinhVucDN.split(",")));
                        List<LinhVuc> linhVucList = new ArrayList<>();
                        for (int i = 0; i < maLinhVucs.size(); i++) {
                            LinhVuc lv = linhVucRepository.findByMaLinhVuc(maLinhVucs.get(i)).orElse(null);
                            if (lv == null) {
                                errors.add(String.format("Lĩnh vực dòng %s không tồn tại.", row));
                            } else {
                                linhVucList.add(lv);
                            }
                        }

                        //TODO: tạo mới doanh nghiệp
                        if (linhVucList.size() != 0) {
                            DoanhNghiep doanhNghiep = new DoanhNghiep();
                            doanhNghiep.setHoTen(hoTenDN);
                            doanhNghiep.setEmail(emailDN);
                            doanhNghiep.setGioiTinh(gioiTinhDN);
                            doanhNghiep.setDienThoai(dienThoaiDN);
                            doanhNghiep.setCongTy(congTyDN);
                            doanhNghiep.setDiaChi(diaChiDN);
                            doanhNghiep.setMaSoThue(maSoThueDN);
                            doanhNghiep.setLinhVucs(linhVucList);
                            doanhNghiep.setRoles(Collections.singletonList(EnumRole.ROLE_DOANH_NGHIEP.name()));
                            doanhNghieps.add(doanhNghiep);
                        }
                    } else {
                        errors.add(String.format("Mã số thuế dòng %s đã tồn tại.", row));
                    }
                } else {
                    errors.add(String.format("Email dòng %s đã tồn tại.", row));
                }
            } else {
                errors.add(String.format("Dữ liệu dòng %s không được bỏ trống.", row));
            }
        }
        doanhNghieps.stream().map(doanhNghiep -> doanhNghiepRepository.save(doanhNghiep))
                .collect(Collectors.toList());
        if (!errors.isEmpty()) {
            throw new InvalidException(errors);
        }

    }

    @Override
    public File exportDanhSachChuyenThamQuanSuDungXeNgoai(List<Map> chuyenThamQuans) throws Exception {
        applyLicense();
        Workbook workbook = new Workbook(new ClassPathResource("/models/bm-danh-sach-chuyen-tham-quan-thue-xe-ngoai-truong.xlsx").getInputStream());
        Worksheet worksheet = workbook.getWorksheets().get(0);
        String fileName = download + "/danh-sach-chuyen-tham-quan-thue-xe-ngoai-truong-" + new Date().getTime() + ".xlsx";
        int location = 7;
        int size = chuyenThamQuans.size();
        if (size < 1) {
            throw new InvalidException(String.format("Không có chuyến tham quan thuê xe ngoài trường"));
        }

        // Content
        for (int i = 0; i < size; i++) {
            location++;
            insertChuyenThamQuan(chuyenThamQuans.get(i), worksheet, location);
        }
        int duyetBGH = size + 9;

        Cells cells = worksheet.getCells();
        cells.merge(duyetBGH, 0, 1, 2);
        cells.merge(duyetBGH, 4, 1, 2);

        worksheet.getCells().get(duyetBGH, 0).putValue("Duyệt BGH");
        worksheet.getCells().get(duyetBGH, 4).putValue("Phòng TBVT");
        worksheet.getCells().get(duyetBGH, 7).putValue("Phòng Quan hệ Doanh nghiệp");

        Style style = worksheet.getCells().getStyle();
        style.getFont().setBold(true);
        worksheet.getCells().get(duyetBGH, 0).setStyle(style, true);
        worksheet.getCells().get(duyetBGH, 4).setStyle(style, true);
        worksheet.getCells().get(duyetBGH, 7).setStyle(style, true);


        // Draw bolder
        Range range = worksheet.getCells().createRange(8, 0, location - 7, 8);
        applyBolderRange(range, worksheet);
        workbook.save(fileName, FileFormatType.XLSX);
        return new File(fileName);

    }

    @Override
    public File exportDanhSachChuyenThamQuanCoCongTacVienAndTinhLuong(List<ChuyenThamQuanTinhLuongDto> chuyenThamQuans, String tenDotThamQuan) throws Exception {
        applyLicense();
        Workbook workbook = new Workbook(new ClassPathResource("/models/bm-luong-ctv.xlsx").getInputStream());
        ReplaceOptions replace = new ReplaceOptions();
        Worksheet worksheet = workbook.getWorksheets().get(0);
        String fileName = download + "/luong-ctv-" + new Date().getTime() + ".xlsx";

        workbook.replace("xx_tenDotThamQuan_xx", "BẢNG TỔNG HỢP DANH SÁCH " + tenDotThamQuan.toUpperCase(), replace);

        int location = 4;
        int size = chuyenThamQuans.size();
        if (size < 1) {
            throw new InvalidException(String.format("Không có danh sách cộng tác viên trong chuyến tham quan"));
        }

        //TODO: fill data
        int stt = 0;
        for (int i = 0; i < size; i++) {
            location++;
            stt++;
            insertLuongCTV(chuyenThamQuans.get(i), worksheet, location, stt);
        }

        //Draw border
        Range range = worksheet.getCells().createRange(5, 0, location - 4, 10);
        applyBolderRange(range, worksheet);
        workbook.save(fileName, FileFormatType.XLSX);

        return new File(fileName);
    }

    @Override
    public File xuatDanhSachSinhVienThamQuan(ChuyenThamQuan chuyenThamQuan) throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        applyLicense();
        Workbook workbook = new Workbook(new ClassPathResource("/models/danh-sach-sinh-vien-tham-quan.xlsx").getInputStream());
        ReplaceOptions replace = new ReplaceOptions();
        Worksheet worksheet = workbook.getWorksheets().get(0);
        String fileName = download + "/danh-sach-sinh-vien-tham-quan-" + new Date().getTime() + ".xlsx";
        List<SinhVien> sinhViens = chuyenThamQuan.getDanhSachSinhViens().stream()
                .filter(e -> e.getSoPhutDiTre() == 0)
                .map(el -> el.getSinhVien()).collect(Collectors.toList());
        int location = 11;
        int stt = 1;
        int size = sinhViens.size();
        if (size < 1) {
            throw new InvalidException("Sinh viên tha gia chuyến tham quan trống!");
        }

        DateTime timeBatDau = new DateTime(chuyenThamQuan.getThoiGianBatDauThamQuan());
        workbook.replace("xxx_thoigian_xxx", chuyenThamQuan.getTenChuyenThamQuan().trim()
                        + " ngày " + timeBatDau.getDay() + "/" + timeBatDau.getMonth() + "/" + timeBatDau.getYear(),
                replace);

        workbook.replace("xxx_giaovienphutrach_xxx", "Giáo viên phụ trách: " + chuyenThamQuan.getGiangVienDangKy().getHoTen(), replace);

        for (int i = 0; i < size; i++) {
            SinhVien sinhVien = sinhViens.get(i);
            insertSinhVien(sinhVien, worksheet, location, stt);
            stt++;
            location++;
        }
        // Draw bolder
        Range range = worksheet.getCells().createRange(11, 0, location - 11, 7);
        applyBolderRange(range, worksheet);
        workbook.save(fileName, FileFormatType.XLSX);
        return new File(fileName);
    }

    @Override
    public File xuatDanhSachSinhVienVang(ChuyenThamQuan chuyenThamQuan) throws Exception {
        applyLicense();
        Workbook workbook = new Workbook(new ClassPathResource("/models/danh-sach-sinh-vien-vang.xlsx").getInputStream());
        ReplaceOptions replace = new ReplaceOptions();
        Worksheet worksheet = workbook.getWorksheets().get(0);
        String fileName = download + "/danh-sach-sinh-vien-vang-" + new Date().getTime() + ".xlsx";
        List<SinhVien> sinhViens = chuyenThamQuan.getDanhSachSinhViens().stream()
                .filter(e -> e.isTrangThai() == false)
                .map(el -> el.getSinhVien())
                .filter(el -> el != null).collect(Collectors.toList());
        int location = 11;
        int stt = 1;
        int size = sinhViens.size();
        if (size == 0) {
            throw new InvalidException("Không có sinh viên vắng!");
        }
        DateTime timeBatDau = new DateTime(chuyenThamQuan.getThoiGianBatDauThamQuan());
        workbook.replace("xxx_thoigian_xxx", chuyenThamQuan.getTenChuyenThamQuan().trim()
                        + " ngày " + timeBatDau.getDay() + "/" + timeBatDau.getMonth() + "/" + timeBatDau.getYear(),
                replace);

        workbook.replace("xxx_giaovienphutrach_xxx", "Giáo viên phụ trách: " + chuyenThamQuan.getGiangVienDangKy().getHoTen(), replace);

        for (int i = 0; i < size; i++) {
            SinhVien sinhVien = sinhViens.get(i);
            insertSinhVien(sinhVien, worksheet, location, stt);
            stt++;
            location++;
        }
        // Draw bolder
        Range range = worksheet.getCells().createRange(11, 0, location - 11, 7);
        applyBolderRange(range, worksheet);
        workbook.save(fileName, FileFormatType.XLSX);
        return new File(fileName);
    }

    @Override
    public void importDanhSachSinhVienDiemDanhBu(ChuyenThamQuan chuyenThamQuan, MultipartFile file) throws Exception {
        InputStream stream = new BufferedInputStream(file.getInputStream());
        Workbook book = new Workbook(stream);
        Worksheet sheet = book.getWorksheets().get(0);
        Cells cells = sheet.getCells();
        Range displayRange = cells.getMaxDisplayRange();
        List<String> errors = new ArrayList<>();
        Date thoiGianKhoiHanh = chuyenThamQuan.getThoiGianKhoiHanh();

        for (int row = displayRange.getFirstRow() + 1; row < displayRange.getRowCount(); row++) {
            String maSV = displayRange.get(row, 1).getStringValue();
            String hoTenSV = displayRange.get(row, 2).getStringValue();
            int soPhutDiTre = Integer.parseInt(displayRange.get(row, 3).getStringValue());
            if (ObjectUtils.isEmpty(maSV) || ObjectUtils.isEmpty(soPhutDiTre) || ObjectUtils.isEmpty(hoTenSV)) {
                errors.add(String.format("Dữ liệu dòng %s không được bỏ trống.", row));
            } else {
                int[] position = {-1};
                SinhVienThamQuan sinhVienThamQuan = chuyenThamQuan.getDanhSachSinhViens().stream()
                        .peek(i -> position[0]++)
                        .filter(svtq -> svtq.getSinhVien().getMaSV().equals(maSV) && sinhVienRepository.findByMaSV(maSV).get().getHoTen().equals(hoTenSV))
                        .findFirst()
                        .orElse(null);
                if (sinhVienThamQuan == null) {
                    errors.add(String.format("Kiểm tra thông tin sinh viên dòng %s.", row));
                } else {
                    sinhVienThamQuan.setThoiGianDiemDanh(new LocalDateTime().toDate());
                    sinhVienThamQuan.setSoPhutDiTre(soPhutDiTre);
                    sinhVienThamQuan.setTrangThai(true);
                    chuyenThamQuan.getDanhSachSinhViens().get(position[0]);
                    chuyenThamQuan.getDanhSachSinhViens().set(position[0], sinhVienThamQuan);
                }
            }
        }
        chuyenThamQuanRepository.save(chuyenThamQuan);

        if (!errors.isEmpty()) {
            throw new InvalidException(errors);
        }

    }


    //<editor-fold desc="private method">
    private void applyBolderRange(Range range, Worksheet worksheet) {
        // create borders
        Style style = worksheet.getCells().getStyle();
        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
        style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
        style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
        StyleFlag styleFlag = new StyleFlag();
        styleFlag.setBorders(true);
        range.applyStyle(style, styleFlag);
    }

    private void applyLicense() throws ServerException {
        try {
            InputStream inputStream = new ClassPathResource("/licenses/license_cells.xml").getInputStream();
            License license = new License();
            license.setLicense(inputStream);
        } catch (Exception e) {
            throw new ServerException("Cannot apply apose cell license !!!!");
        }
    }
    //</editor-fold>


    private void insertChuyenThamQuan(Map chuyenThamQuan, Worksheet worksheet, int location) throws Exception {
        worksheet.autoFitRow(location);

        worksheet.getCells().get(location, 0).putValue(chuyenThamQuan.get("STT"));
        worksheet.getCells().get(location, 1).putValue(chuyenThamQuan.get("Ngay"));
        worksheet.getCells().get(location, 2).putValue(chuyenThamQuan.get("So sinh vien"));
        worksheet.getCells().get(location, 3).putValue(chuyenThamQuan.get("Xe 16 cho"));
        worksheet.getCells().get(location, 4).putValue(chuyenThamQuan.get("Xe 29 cho"));
        worksheet.getCells().get(location, 5).putValue(chuyenThamQuan.get("Xe 45 cho"));
        worksheet.getCells().get(location, 6).putValue(chuyenThamQuan.get("Noi den"));
        worksheet.getCells().get(location, 7).putValue(chuyenThamQuan.get("Thoi gian"));
        AutoFitterOptions options = new AutoFitterOptions();
        options.setAutoFitMergedCells(true);
        worksheet.autoFitRows(options);
    }

    private void insertSinhVien(SinhVien sinhVien, Worksheet worksheet,
                                int location, int soThuTu) throws Exception {
        worksheet.getCells().get(location, 0).putValue(soThuTu);
        worksheet.getCells().get(location, 1).putValue(sinhVien.getMaSV());
        worksheet.getCells().get(location, 2).putValue(sinhVien.getHoTen());
        worksheet.getCells().get(location, 3).putValue(sinhVien.getNgaySinh());
        worksheet.getCells().get(location, 4).putValue(sinhVien.getLop());
        worksheet.getCells().get(location, 5).putValue(sinhVien.getEmail());
        worksheet.getCells().get(location, 6).putValue(sinhVien.getDienThoai());
        worksheet.autoFitRow(location);
        AutoFitterOptions options = new AutoFitterOptions();
        options.setAutoFitMergedCells(true);
        worksheet.autoFitRows(options);

    }

    private void insertLuongCTV(ChuyenThamQuanTinhLuongDto chuyenThamQuan, Worksheet worksheet, int location, int stt) throws Exception {
        worksheet.autoFitRow(location);

        List<CongTacVien> congTacViens = chuyenThamQuan.getCongTacViens();

        worksheet.getCells().get(location, 0).putValue(stt);
        worksheet.getCells().get(location, 1).putValue(congTacViens.stream().map(s -> s.getHoTen()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "").replaceAll(", ", "\n"));
        worksheet.getCells().get(location, 2).putValue(congTacViens.stream().map(s -> s.getMaSV()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "").replaceAll(", ", "\n"));
        worksheet.getCells().get(location, 3).putValue(chuyenThamQuan.getCongTy().getCongTy());
        worksheet.getCells().get(location, 4).putValue(chuyenThamQuan.getNgayThamQuan());
        worksheet.getCells().get(location, 5).putValue(timeFormat.format(chuyenThamQuan.getThoiGianBatDauThamQuan()) + " - " + timeFormat.format(chuyenThamQuan.getThoiGianKetThucThamQuan()));
        worksheet.getCells().get(location, 6).putValue(chuyenThamQuan.getTongSoGio());
        worksheet.getCells().get(location, 7).putValue(chuyenThamQuan.getDonGia());
        worksheet.getCells().get(location, 8).putValue(chuyenThamQuan.getThanhTien());
//        worksheet.getCells().get(location, 9).putValue(chuyenThamQuan.get("KY NHAN").toString().replace("[","").replace("]","").replaceAll(",","\r\n"));

        AutoFitterOptions options = new AutoFitterOptions();
        options.setAutoFitMergedCells(true);
        worksheet.autoFitRows(options);
    }
}