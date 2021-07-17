package com.stc.thamquan.services.word;

import com.aspose.words.*;
import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.DotThamQuan;
import com.stc.thamquan.entities.GiangVien;
import com.stc.thamquan.entities.SinhVien;
import com.stc.thamquan.entities.embedded.SinhVienThamQuan;
import com.stc.thamquan.services.giangvien.GiangVienService;
import com.stc.thamquan.services.sinhvien.SinhVienService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:47
 * Filename  : WordServiceImpl
 */
@Slf4j
@Service
public class WordServiceImpl implements WordService {
    @Value("${file.download_dir}")
    private String download;

    private DateFormat timeFormat = new SimpleDateFormat("hh:mm");

    private DateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final GiangVienService giangVienService;

    private final SinhVienService sinhVienService;

    private DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");

    private DateFormat dateFormatDetail = new SimpleDateFormat("EEEE");

    public WordServiceImpl(GiangVienService giangVienService, SinhVienService sinhVienService) {
        this.giangVienService = giangVienService;
        this.sinhVienService = sinhVienService;
    }

    @Override
    public File xuatCongVanXinThamQuanGuiDoanhNghiep(ChuyenThamQuan chuyenThamQuan) throws Exception {
        applyLicense();
        String fileName = download + "/cong-van-gui-doanh-nghiep" + "-" + chuyenThamQuan.getId() + "-" + new Date().getTime() + ".docx";
        Document document = new Document(new ClassPathResource("/models/template_mau_CV_xin_tham_quan_2020.doc").getInputStream());
        FindReplaceOptions findReplaceOptions = new FindReplaceOptions();
        LocalDate current = LocalDate.now();
     String phuongTien = chuyenThamQuan.getPhuongTien().stream().map(pt-> pt.getTenXe()).collect(Collectors.joining(", "));
        //replace text
        document.getRange().replace("xx__ngay__xx", current.getDayOfMonth() + "", findReplaceOptions);
        document.getRange().replace("xx__thang__xx", current.getMonthOfYear() + "", findReplaceOptions);
        document.getRange().replace("xx__nam__xx", current.getYear() + "", findReplaceOptions);
        document.getRange().replace("xx__doanhNghiep__xx", chuyenThamQuan.getDoanhNghiep().getHoTen(), findReplaceOptions);
        document.getRange().replace("xx__phuongTien__xx", phuongTien, findReplaceOptions);

        //insert table thông tin và thời gian đề xuất
        int soGiangVienThamGias = chuyenThamQuan.getDanhSachGiangVienThamGias().size();
        Table tableThongTin = (Table) document.getChild(NodeType.TABLE, 0, true);
        Row startRow = tableThongTin.getLastRow();
        Row endRow = tableThongTin.getLastRow();

        for (int i = 0; i < soGiangVienThamGias; i++) {
            Row row = tableThongTin.getLastRow();
            Row cloneRow = (Row) row.deepClone(true);
            row.getCells().get(0).getFirstParagraph().appendChild(new Run(document, i + 1 + "."));
            GiangVien giangVien = giangVienService.getGiangVienByIdCore(chuyenThamQuan.getDanhSachGiangVienThamGias().get(i));
            if (giangVien != null) {
                row.getCells().get(1).getFirstParagraph().appendChild(new Run(document, giangVien.getHoTen() + ""));
            }
            row.getCells().get(2).getFirstParagraph().appendChild(new Run(document, giangVien.getNganh().getTenNganh() + ""));
            row.getCells().get(3).getFirstParagraph().appendChild(new Run(document, chuyenThamQuan.getDanhSachSinhViens().size() + ""));
            row.getCells().get(4).getFirstParagraph().appendChild(new Run(document, chuyenThamQuan.getDotThamQuan().getNamHoc() + ""));
            row.getCells().get(5).getFirstParagraph().appendChild(new Run(document, dateFormat.format(chuyenThamQuan.getThoiGianDuKien()) + ""));

            tableThongTin.appendChild(cloneRow);
            endRow = tableThongTin.getLastRow();
        }
        mergeCells(startRow.getCells().get(3), endRow.getCells().get(3));
        mergeCells(startRow.getCells().get(4), endRow.getCells().get(4));
        mergeCells(startRow.getCells().get(5), endRow.getCells().get(5));
        tableThongTin.getLastRow().remove();

        document.save(fileName.trim(), SaveFormat.DOCX);
        return new File(fileName);
    }

    @Override
    public File xuatKeHoachThamQuan(DotThamQuan dotThamQuan, List<ChuyenThamQuan> chuyenThamQuans) throws Exception {
        applyLicense();
        String fileName = download + "/ke-hoach-gui-doanh-nghiep-dot-" + String.join("-", dotThamQuan.getTenDotThamQuan().split(" ")) + "-" + new Date().getTime() + ".docx";
        Document document = new Document(new ClassPathResource("/models/bm-ke-hoach-tham-quan.doc").getInputStream());
        FindReplaceOptions findReplaceOptions = new FindReplaceOptions();
        LocalDate current = LocalDate.now();
        int soLuongChuyenThamQuans = chuyenThamQuans.size();

        //replace text
        document.getRange().replace("xx__ngay__xx", current.getDayOfMonth() + "", findReplaceOptions);
        document.getRange().replace("xx__thang__xx", current.getMonthOfYear() + "", findReplaceOptions);
        document.getRange().replace("xx__nam__xx", current.getYear() + "", findReplaceOptions);
        document.getRange().replace("xx__hocKy__xx", dotThamQuan.getHocKy(), findReplaceOptions);
        document.getRange().replace("xx__namHoc__xx", dotThamQuan.getNamHoc(), findReplaceOptions);
        document.getRange().replace("xx__mucDich__xx", dotThamQuan.getMucDich(), findReplaceOptions);
        document.getRange().replace("xx__yeuCau__xx", dotThamQuan.getYeuCau(), findReplaceOptions);
        document.getRange().replace("xx__noiDungThamQuan__xx", dotThamQuan.getNoiDungThamQuan(), findReplaceOptions);
        document.getRange().replace("xx__dot__xx", dotThamQuan.getTenDotThamQuan(), findReplaceOptions);
        document.getRange().replace("xx__soLuongChuyenThamQuan__xx", soLuongChuyenThamQuans + "", findReplaceOptions);

        Table tableChuyenThamQuan = (Table) document.getChild(NodeType.TABLE, 0, true);
        for (int i = 0; i < soLuongChuyenThamQuans; i++) {
            Row row = tableChuyenThamQuan.getLastRow();
            Row rowClone = (Row) row.deepClone(true);
            ChuyenThamQuan chuyenThamQuan = chuyenThamQuans.get(i);
            List<String> tenGiangViens = new ArrayList<>();
            List<String> nganhHocs = new ArrayList<>();
            chuyenThamQuan.getDanhSachGiangVienThamGias().forEach(el -> {
                GiangVien giangVien = giangVienService.getGiangVienByIdCore(el);
                if (giangVien == null)
                    return;
                tenGiangViens.add(giangVien.isGioiTinh() ? "Cô " + giangVien.getHoTen() : "Thầy " + giangVien.getHoTen());
                nganhHocs.add(giangVien.getNganh().getTenNganh());
            });
            String thoiGianBatDauThamQuan = dateFormat.format(chuyenThamQuan.getThoiGianBatDauThamQuan());
            String thoiGianKetThucThamQuan = dateFormat.format(chuyenThamQuan.getThoiGianKetThucThamQuan());

            String thoiGian = thoiGianBatDauThamQuan.split(" ")[0] + " - "
                    + thoiGianKetThucThamQuan.split(" ")[0] + ControlChar.LINE_BREAK
                    + thoiGianBatDauThamQuan.split(" ")[1] + ControlChar.LINE_BREAK
                    + "(" + dateFormatDetail.format(chuyenThamQuan.getThoiGianBatDauThamQuan()) + ")";
            row.getCells().get(0).getFirstParagraph().appendChild(new Run(document, i + 1 + "."));
            row.getCells().get(1).getFirstParagraph().appendChild(new Run(document, chuyenThamQuan.getDoanhNghiep().getCongTy()));
            row.getCells().get(2).getFirstParagraph().appendChild(new Run(document, chuyenThamQuan.getDoanhNghiep().getDiaChi()));
            row.getCells().get(3).getFirstParagraph().appendChild(new Run(document, String.join(" & ", tenGiangViens)));
            row.getCells().get(4).getFirstParagraph().appendChild(new Run(document, String.join(" & ", nganhHocs.stream().distinct().collect(Collectors.toList()))));
            row.getCells().get(5).getFirstParagraph().appendChild(new Run(document, chuyenThamQuan.getDanhSachGiangVienThamGias().size() + ""));
            row.getCells().get(6).getFirstParagraph().appendChild(new Run(document, thoiGian));
            tableChuyenThamQuan.appendChild(rowClone);
        }
        tableChuyenThamQuan.getLastRow().remove();
        document.save(fileName.trim(), SaveFormat.DOCX);
        return new File(fileName);
    }

    public File xuatPhieuXacNhanThamQuan(ChuyenThamQuan chuyenThamQuan) throws Exception {
        applyLicense();

        String fileName = download + "/giay-xac-nhan-tham-quan" + "-" + chuyenThamQuan.getId() + ".docx";
        Document document = new Document(new ClassPathResource("/models/template_giay_xac_nhan_tham_quan.docx").getInputStream());
        FindReplaceOptions findReplaceOptions = new FindReplaceOptions();

        //get list sv
        List<SinhVienThamQuan> danhSachSVThamQuan = chuyenThamQuan.getDanhSachSinhViens();
        List<SinhVien> danhSachSinhVien = new ArrayList<>();

        danhSachSVThamQuan.forEach(svtq -> {
            SinhVien sinhVien = svtq.getSinhVien();
            danhSachSinhVien.add(sinhVien);
        });


        //replace text
        if (chuyenThamQuan.getDoanhNghiep().getCongTy() != null) {
            document.getRange().replace("xx_congTy_xx", chuyenThamQuan.getDoanhNghiep().getCongTy(), findReplaceOptions);
            document.getRange().replace("xx_congTyUPCASE_xx", chuyenThamQuan.getDoanhNghiep().getCongTy().toUpperCase(), findReplaceOptions);
        } else {
            document.getRange().replace("xx_congTy_xx", "Công Ty", findReplaceOptions);
            document.getRange().replace("xx_congTyUPCASE_xx", "CÔNG TY", findReplaceOptions);
        }

        document.getRange().replace("xx_slSinhVien_xx", String.valueOf(chuyenThamQuan.getDanhSachSinhViens().size()), findReplaceOptions);

        if (chuyenThamQuan.getDoanhNghiep().getDiaChi() != null) {
            document.getRange().replace("xx_diaChi_xx", chuyenThamQuan.getDoanhNghiep().getDiaChi(), findReplaceOptions);
        } else {
            document.getRange().replace("xx_diaChi_xx", "N/A", findReplaceOptions);
        }

        document.getRange().replace("xx_batDau_xx", timeFormat.format(chuyenThamQuan.getThoiGianBatDauThamQuan()), findReplaceOptions);
        document.getRange().replace("xx_ketThuc_xx", timeFormat.format(chuyenThamQuan.getThoiGianKetThucThamQuan()), findReplaceOptions);
        document.getRange().replace("xx_ngayThamQuan_xx", shortDateFormat.format(chuyenThamQuan.getThoiGianBatDauThamQuan()), findReplaceOptions);
        document.getRange().replace("xx_khoa_xx", danhSachSinhVien.get(0).getNganh().getKhoa().getTenKhoa().toUpperCase(), findReplaceOptions);
        document.getRange().replace("xx_lop_xx", danhSachSinhVien.get(0).getLop(), findReplaceOptions);

        //insert table danh sách sinh viên tham quan
        Table tableDanhSach = (Table) document.getChild(NodeType.TABLE, 0, true);

        for (int i = 0; i < danhSachSinhVien.size(); i++) {
            Row row = tableDanhSach.getLastRow();
            Row cloneRow = (Row) row.deepClone(true);
            row.getCells().get(0).getFirstParagraph().appendChild(new Run(document, i + 1 + ""));
            row.getCells().get(1).getFirstParagraph().appendChild(new Run(document, danhSachSinhVien.get(i).getMaSV()));
            row.getCells().get(2).getFirstParagraph().appendChild(new Run(document, danhSachSinhVien.get(i).getHoTen()));

            if (danhSachSinhVien.get(i).getCmnd() != null) {
                row.getCells().get(3).getFirstParagraph().appendChild(new Run(document, danhSachSinhVien.get(i).getCmnd()));
            }
            if (danhSachSinhVien.get(i).getDienThoai() != null) {
                row.getCells().get(4).getFirstParagraph().appendChild(new Run(document, danhSachSinhVien.get(i).getDienThoai()));
            }
            if (danhSachSinhVien.get(i).getEmail() != null) {
                row.getCells().get(5).getFirstParagraph().appendChild(new Run(document, danhSachSinhVien.get(i).getEmail()));
            }

            tableDanhSach.appendChild(cloneRow);
        }

        tableDanhSach.getLastRow().remove();
        document.save(fileName.trim(), SaveFormat.DOCX);

        return new File(fileName);
    }

    /***
     *
     * @param startCell : Cell bắt đầu merge
     * @param endCell   : merge đến cell
     */
    // Util methods
    public static void mergeCells(Cell startCell, Cell endCell) {
        Table parentTable = startCell.getParentRow().getParentTable();

        // Find the row and cell indices for the start and end cell.
        Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell), parentTable.indexOf(startCell.getParentRow()));
        Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell), parentTable.indexOf(endCell.getParentRow()));
        // Create the range of cells to be merged based off these indices. Inverse each index if the end cell if before the start cell.
        Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x), Math.min(startCellPos.y, endCellPos.y), Math.abs(endCellPos.x - startCellPos.x) + 1,
                Math.abs(endCellPos.y - startCellPos.y) + 1);

        for (Row row : parentTable.getRows()) {
            for (Cell cell : row.getCells()) {
                Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));

                // Check if the current cell is inside our merge range then merge it.
                if (mergeRange.contains(currentPos)) {
                    if (currentPos.x == mergeRange.x)
                        cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

                    if (currentPos.y == mergeRange.y)
                        cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                }
            }
        }
    }//ExEnd:mergeCells

    public static void insertDocument(Node insertAfterNode, Document srcDoc) throws Exception {
        // Make sure that the node is either a paragraph or table.
        if ((insertAfterNode.getNodeType() != NodeType.PARAGRAPH) & (insertAfterNode.getNodeType() != NodeType.TABLE))
            throw new IllegalArgumentException("The destination node should be either a paragraph or table.");

        // We will be inserting into the parent of the destination paragraph.
        CompositeNode dstStory = insertAfterNode.getParentNode();

        // This object will be translating styles and lists during the import.
        NodeImporter importer = new NodeImporter(srcDoc, insertAfterNode.getDocument(), ImportFormatMode.USE_DESTINATION_STYLES);

        // Loop through all sections in the source document.
        for (Section srcSection : srcDoc.getSections()) {
            // Loop through all block level nodes (paragraphs and tables) in the body of the section.
            for (Node srcNode : (Iterable<Node>) srcSection.getBody()) {
                // Let's skip the node if it is a last empty paragraph in a section.
                if (srcNode.getNodeType() == (NodeType.PARAGRAPH)) {
                    Paragraph para = (Paragraph) srcNode;
                    if (para.isEndOfSection() && !para.hasChildNodes())
                        continue;
                }

                // This creates a clone of the node, suitable for insertion into the destination document.
                Node newNode = importer.importNode(srcNode, true);
                // Insert new node after the reference node.
                dstStory.insertAfter(newNode, insertAfterNode);
                insertAfterNode = newNode;
            }
        }
    }

    private void applyLicense() throws Exception {
        InputStream inLicense = new ClassPathResource("/licenses/license.xml").getInputStream();
        License license = new com.aspose.words.License();
        license.setLicense(inLicense);
    }
}
