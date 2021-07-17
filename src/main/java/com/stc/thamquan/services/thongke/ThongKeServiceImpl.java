package com.stc.thamquan.services.thongke;

import com.mongodb.BasicDBObject;
import com.stc.thamquan.dtos.Total;
import com.stc.thamquan.dtos.thongke.*;
import com.stc.thamquan.entities.*;
import com.stc.thamquan.entities.embedded.KetQuaKhaoSat;
import com.stc.thamquan.exceptions.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/17/21
 * Time      : 15:19
 * Filename  : ThongKeServiceImpl
 */
@Slf4j
@Service
public class ThongKeServiceImpl implements ThongKeService {

    private final MongoTemplate mongoTemplate;

    public ThongKeServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int thongKeTongChuyenThamQuan(Date tuNgay, Date denNgay) {
        if (ObjectUtils.isEmpty(tuNgay) || ObjectUtils.isEmpty(denNgay)) {
            throw new InvalidException("Khoảng thời gian thống kê không được bỏ trống.");
        }
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();

        searchCriterias.add(Criteria.where("thoiGianKhoiHanh").gte(tuNgay));
        searchCriterias.add(Criteria.where("thoiGianKhoiHanh").lte(denNgay));
        searchCriterias.add(Criteria.where("trangThai").is("HOAN_TAT"));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        CountOperation countOperation = count().as("total");

        Aggregation aggregation = newAggregation(
                matchOperation,
                countOperation
        );
        AggregationResults<Total> aggregate = mongoTemplate.aggregate(aggregation,
                ChuyenThamQuan.class,
                Total.class);
        List<Total> total = aggregate.getMappedResults();
        if (total.size() == 0) {
            return 0;
        }
        return total.get(0).getTotal();
    }

    @Override
    public List<ThongKeChuyenThamQuanTheoDoanhNghiep> thongKeChuyenThamQuanTheoDoanhNghiep(Date tuNgay, Date denNgay) {
        if (ObjectUtils.isEmpty(tuNgay) || ObjectUtils.isEmpty(denNgay)) {
            throw new InvalidException("Khoảng thời gian thống kê không được bỏ trống.");
        }
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();

        searchCriterias.add(Criteria.where("thoiGianKhoiHanh").gte(tuNgay));
        searchCriterias.add(Criteria.where("thoiGianKhoiHanh").lte(denNgay));
        searchCriterias.add(Criteria.where("trangThai").is("HOAN_TAT"));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        GroupOperation groupOperation = group("$doanhNghiepId").count().as("soLuong");
        LookupOperation lookupOperation = lookup(
                "doanh-nghiep",
                "_id",
                "_id",
                "doanhNghiep");
        ProjectionOperation projectionOperation = Aggregation.project("doanhNghiep.$id")
                .and(ConvertOperators.ToObjectId.toObjectId("$doanhNghiep.$id")).as("doanhNghiepId");

        ProjectionOperation projectionOperation1 = Aggregation.project("doanhNghiep", "soLuong");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "soLuong");

        Aggregation aggregation = newAggregation(
                matchOperation,
                projectionOperation,
                groupOperation,
                lookupOperation,
                projectionOperation1,
                sortOperation
        );
        AggregationResults<ThongKeChuyenThamQuanTheoDoanhNghiep> aggregate = mongoTemplate.aggregate(aggregation,
                ChuyenThamQuan.class,
                ThongKeChuyenThamQuanTheoDoanhNghiep.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("NHAP_LIEU"));
        searchCriterias.add(Criteria.where("cauTraLoi").exists(true).ne(""));
        if(!ObjectUtils.isEmpty(chuyenThamQuanId)){
            searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        }
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("cauTraLoi");
        GroupOperation groupOperation = group("cauHoi")
                .push("cauTraLoi").as("cauTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("cauTraLoi")
                .and("_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");
        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatDoanhNghiep.class,
                KetQuaKhaoSatDoanhNghiepLoaiCauHoiNhapLieu.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiLuaChon(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_MOT"),
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_NHIEU")
        );

        criteria.andOperator(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("cauTraLoi");
        GroupOperation groupOperation = group("cauHoi", "cauTraLoi").count().as("soLuotTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("soLuotTraLoi")
                .and("_id.cauHoi").as("cauHoi")
                .and("_id.cauTraLoi").as("cauTraLoi");
        GroupOperation groupOperation1 = group("cauHoi")
                .push(new BasicDBObject("soLuotTraLoi", "$soLuotTraLoi")
                        .append("cauTraLoi", "$cauTraLoi")).as("soLuotTraLoiList")
                .sum("soLuotTraLoi").as("total");
        UnwindOperation unwindOperation1 = unwind("soLuotTraLoiList");
        AddFieldsOperation addFieldsOperation = addFields().addFieldWithValue("soLuotTraLoiList.tiLe",
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf("$soLuotTraLoiList.soLuotTraLoi")
                                        .divideBy("$total")
                        ).multiplyBy(100)).roundToPlace(2))
                .build();
        GroupOperation groupOperation2 = group("_id", "total")
                .push("soLuotTraLoiList").as("soLuotTraLoiList");
        ProjectionOperation projectionOperation1 = Aggregation.project("soLuotTraLoiList")
                .and("_id._id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                groupOperation1,
                unwindOperation1,
                addFieldsOperation,
                groupOperation2,
                projectionOperation1,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatDoanhNghiep.class,
                KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep> thongKeKetQuaKhaoSatDoanhNghiepTheoLoaiCauHoiSapXep(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("SAP_XEP"));
        searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("$cauTraLoi", "thuTuUuTien");
        GroupOperation groupOperation = group("$cauHoi", "$cauTraLoi", "thuTuUuTien")
                .count().as("soLuotChon");
        GroupOperation groupOperation1 = group("$_id.cauHoi", "$_id.cauTraLoi")
                .push(new BasicDBObject("thuTuUuTien", "$_id.thuTuUuTien")
                        .append("soLuotChon", "$soLuotChon"))
                .as("thuTuUuTiens");
        GroupOperation groupOperation2 = group("$cauHoi")
                .push(new BasicDBObject("dapAn", "$_id.cauTraLoi")
                        .append("thuTuUuTiens", "$thuTuUuTiens"))
                .as("cauTraLois");
        ProjectionOperation projectionOperation = project("cauTraLois")
                .and("$_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                groupOperation1,
                groupOperation2,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatDoanhNghiep.class,
                KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep.class);
        List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep> ketQuaKhaoSatDoanhNghiepLoaiCauHoiSapXeps = aggregate.getMappedResults();

        // them cac vi tri co so luot chon = 0
        List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep> results = new ArrayList<>();
        for (int i = 0; i < ketQuaKhaoSatDoanhNghiepLoaiCauHoiSapXeps.size(); i++) {
            KetQuaKhaoSatDoanhNghiepLoaiCauHoiSapXep ketQuaKhaoSat = ketQuaKhaoSatDoanhNghiepLoaiCauHoiSapXeps.get(i);
            List<CauTraLoi> ketQuas = new ArrayList<>();
            for (int j = 0; j < ketQuaKhaoSat.getCauTraLois().size(); j++) {
                CauTraLoi cauTraLoi = ketQuaKhaoSat.getCauTraLois().get(j);
                List<ThuTuUuTien> thuTuUuTiens = ketQuaKhaoSat.getCauTraLois().get(j).getThuTuUuTiens();
                for (int k = 0; k < ketQuaKhaoSat.getCauHoi().getDanhSachLuaChon().size(); k++) {
                    int finalK = k;
                    if (!thuTuUuTiens.stream().anyMatch(el -> el.getThuTuUuTien() == finalK)) {
                        thuTuUuTiens.add(new ThuTuUuTien(k, 0));
                    }
                }
                cauTraLoi.setThuTuUuTiens(thuTuUuTiens.stream()
                        .sorted(Comparator.comparingInt(ThuTuUuTien::getThuTuUuTien))
                        .collect(Collectors.toList()));
                ketQuas.add(cauTraLoi);
            }
            ketQuaKhaoSat.setCauTraLois(ketQuas.stream()
                    .sorted(Comparator.comparing(CauTraLoi::getDapAn))
                    .collect(Collectors.toList()));
            results.add(ketQuaKhaoSat);
        }
        return results;
    }

    @Override
    public List<KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("NHAP_LIEU"));
        searchCriterias.add(Criteria.where("cauTraLoi").exists(true).ne(""));
        searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("$cauTraLoi");
        GroupOperation groupOperation = group("$cauHoi")
                .push("cauTraLoi").as("cauTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("cauTraLoi")
                .and("_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");
        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatSinhVien.class,
                KetQuaKhaoSatSinhVienLoaiCauHoiNhapLieu.class);

        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatSinhVienLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiLuaChon(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_MOT"),
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_NHIEU")
        );

        criteria.andOperator(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("cauTraLoi");
        GroupOperation groupOperation = group("cauHoi", "cauTraLoi").count().as("soLuotTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("soLuotTraLoi")
                .and("_id.cauHoi").as("cauHoi")
                .and("_id.cauTraLoi").as("cauTraLoi");
        GroupOperation groupOperation1 = group("cauHoi")
                .push(new BasicDBObject("soLuotTraLoi", "$soLuotTraLoi")
                        .append("cauTraLoi", "$cauTraLoi")).as("soLuotTraLoiList")
                .sum("soLuotTraLoi").as("total");
        UnwindOperation unwindOperation1 = unwind("soLuotTraLoiList");
        AddFieldsOperation addFieldsOperation = addFields().addFieldWithValue("soLuotTraLoiList.tiLe",
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf("$soLuotTraLoiList.soLuotTraLoi")
                                        .divideBy("$total")
                        ).multiplyBy(100)).roundToPlace(2))
                .build();

        GroupOperation groupOperation2 = group("_id", "total")
                .push("soLuotTraLoiList").as("soLuotTraLoiList");
        ProjectionOperation projectionOperation1 = Aggregation.project("soLuotTraLoiList")
                .and("_id._id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                groupOperation1,
                unwindOperation1,
                addFieldsOperation,
                groupOperation2,
                projectionOperation1,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatSinhVienLoaiCauHoiLuaChon> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatSinhVien.class,
                KetQuaKhaoSatSinhVienLoaiCauHoiLuaChon.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep> thongKeKetQuaKhaoSatSinhVienTheoLoaiCauHoiSapXep(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("SAP_XEP"));
        searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("$cauTraLoi", "thuTuUuTien");
        GroupOperation groupOperation = group("$cauHoi", "$cauTraLoi", "thuTuUuTien")
                .count().as("soLuotChon");
        GroupOperation groupOperation1 = group("$_id.cauHoi", "$_id.cauTraLoi")
                .push(new BasicDBObject("thuTuUuTien", "$_id.thuTuUuTien")
                        .append("soLuotChon", "$soLuotChon"))
                .as("thuTuUuTiens");
        GroupOperation groupOperation2 = group("$cauHoi")
                .push(new BasicDBObject("dapAn", "$_id.cauTraLoi")
                        .append("thuTuUuTiens", "$thuTuUuTiens"))
                .as("cauTraLois");
        ProjectionOperation projectionOperation = project("cauTraLois")
                .and("$_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                groupOperation1,
                groupOperation2,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatSinhVien.class,
                KetQuaKhaoSatSinhVienLoaiCauHoiSapXep.class);
        List<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep> ketQuaKhaoSatSinhVienLoaiCauHoiSapXeps = aggregate.getMappedResults();

        // them cac vi tri co so luot chon = 0
        List<KetQuaKhaoSatSinhVienLoaiCauHoiSapXep> results = new ArrayList<>();
        for (int i = 0; i < ketQuaKhaoSatSinhVienLoaiCauHoiSapXeps.size(); i++) {
            KetQuaKhaoSatSinhVienLoaiCauHoiSapXep ketQuaKhaoSat = ketQuaKhaoSatSinhVienLoaiCauHoiSapXeps.get(i);
            List<CauTraLoi> ketQuas = new ArrayList<>();
            for (int j = 0; j < ketQuaKhaoSat.getCauTraLois().size(); j++) {
                CauTraLoi cauTraLoi = ketQuaKhaoSat.getCauTraLois().get(j);
                List<ThuTuUuTien> thuTuUuTiens = ketQuaKhaoSat.getCauTraLois().get(j).getThuTuUuTiens();
                for (int k = 0; k < ketQuaKhaoSat.getCauHoi().getDanhSachLuaChon().size(); k++) {
                    int finalK = k;
                    if (!thuTuUuTiens.stream().anyMatch(el -> el.getThuTuUuTien() == finalK)) {
                        thuTuUuTiens.add(new ThuTuUuTien(k, 0));
                    }
                }
                cauTraLoi.setThuTuUuTiens(thuTuUuTiens.stream()
                        .sorted(Comparator.comparingInt(ThuTuUuTien::getThuTuUuTien))
                        .collect(Collectors.toList()));
                ketQuas.add(cauTraLoi);
            }
            ketQuaKhaoSat.setCauTraLois(ketQuas.stream()
                    .sorted(Comparator.comparing(CauTraLoi::getDapAn))
                    .collect(Collectors.toList()));
            results.add(ketQuaKhaoSat);
        }
        return results;
    }

    @Override
    public List<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> thongKeMucDoHaiLongCuaDoanhNghiep() {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_MOT"),
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_NHIEU")
        );
        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("cauTraLoi");
        GroupOperation groupOperation = group("cauHoi", "cauTraLoi").count().as("soLuotTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("soLuotTraLoi")
                .and("_id.cauHoi").as("cauHoi")
                .and("_id.cauTraLoi").as("cauTraLoi");
        GroupOperation groupOperation1 = group("cauHoi")
                .push(new BasicDBObject("soLuotTraLoi", "$soLuotTraLoi")
                        .append("cauTraLoi", "$cauTraLoi")).as("soLuotTraLoiList")
                .sum("soLuotTraLoi").as("total");
        UnwindOperation unwindOperation1 = unwind("soLuotTraLoiList");
        AddFieldsOperation addFieldsOperation = addFields().addFieldWithValue("soLuotTraLoiList.tiLe",
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf("$soLuotTraLoiList.soLuotTraLoi")
                                        .divideBy("$total")
                        ).multiplyBy(100)).roundToPlace(2))
                .build();
        GroupOperation groupOperation2 = group("_id", "total")
                .push("soLuotTraLoiList").as("soLuotTraLoiList");
        ProjectionOperation projectionOperation1 = Aggregation.project("soLuotTraLoiList")
                .and("_id._id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");


        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                groupOperation1,
                unwindOperation1,
                addFieldsOperation,
                groupOperation2,
                projectionOperation1,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatDoanhNghiep.class,
                KetQuaKhaoSatDoanhNghiepLoaiCauHoiLuaChon.class);
        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatCongTacVienLoaiCauHoiNhapLieu> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiNhapLieu(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("NHAP_LIEU"));
        searchCriterias.add(Criteria.where("cauTraLoi").exists(true).ne(""));
        searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("$cauTraLoi");
        GroupOperation groupOperation = group("$cauHoi")
                .push("cauTraLoi").as("cauTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("cauTraLoi")
                .and("_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");
        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatCongTacVienLoaiCauHoiNhapLieu> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatCongTacVien.class,
                KetQuaKhaoSatCongTacVienLoaiCauHoiNhapLieu.class);

        return aggregate.getMappedResults();
    }

    @Override
    public List<KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiLuaChon(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_MOT"),
                Criteria.where("cauHoi.loaiCauHoi").is("CHON_NHIEU")
        );

        criteria.andOperator(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("cauTraLoi");
        GroupOperation groupOperation = group("cauHoi", "cauTraLoi").count().as("soLuotTraLoi");
        ProjectionOperation projectionOperation = Aggregation.project("soLuotTraLoi")
                .and("_id.cauHoi").as("cauHoi")
                .and("_id.cauTraLoi").as("cauTraLoi");
        GroupOperation groupOperation1 = group("cauHoi")
                .push(new BasicDBObject("soLuotTraLoi", "$soLuotTraLoi")
                        .append("cauTraLoi", "$cauTraLoi")).as("soLuotTraLoiList")
                .sum("soLuotTraLoi").as("total");
        UnwindOperation unwindOperation1 = unwind("soLuotTraLoiList");
        AddFieldsOperation addFieldsOperation = addFields().addFieldWithValue("soLuotTraLoiList.tiLe",
                ArithmeticOperators.valueOf(
                        ArithmeticOperators.valueOf(
                                ArithmeticOperators.valueOf("$soLuotTraLoiList.soLuotTraLoi")
                                        .divideBy("$total")
                        ).multiplyBy(100)).roundToPlace(2))
                .build();

        GroupOperation groupOperation2 = group("_id", "total")
                .push("soLuotTraLoiList").as("soLuotTraLoiList");
        ProjectionOperation projectionOperation1 = Aggregation.project("soLuotTraLoiList")
                .and("_id._id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                groupOperation1,
                unwindOperation1,
                addFieldsOperation,
                groupOperation2,
                projectionOperation1,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatCongTacVien.class,
                KetQuaKhaoSatCongTacVienLoaiCauHoiLuaChon.class);
        return aggregate.getMappedResults();    }

    @Override
    public List<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep> thongKeKetQuaKhaoSatCongTacVienTheoLoaiCauHoiSapXep(String chuyenThamQuanId) {
        Criteria criteria = new Criteria();
        List<Criteria> searchCriterias = new ArrayList<>();
        searchCriterias.add(Criteria.where("cauHoi.loaiCauHoi").is("SAP_XEP"));
        searchCriterias.add(Criteria.where("chuyenThamQuan.$id").is(new ObjectId(chuyenThamQuanId)));
        criteria.andOperator(searchCriterias.toArray(new Criteria[searchCriterias.size()]));

        MatchOperation matchOperation = match(criteria);
        UnwindOperation unwindOperation = unwind("$cauTraLoi", "thuTuUuTien");
        GroupOperation groupOperation = group("$cauHoi", "$cauTraLoi", "thuTuUuTien")
                .count().as("soLuotChon");
        GroupOperation groupOperation1 = group("$_id.cauHoi", "$_id.cauTraLoi")
                .push(new BasicDBObject("thuTuUuTien", "$_id.thuTuUuTien")
                        .append("soLuotChon", "$soLuotChon"))
                .as("thuTuUuTiens");
        GroupOperation groupOperation2 = group("$cauHoi")
                .push(new BasicDBObject("dapAn", "$_id.cauTraLoi")
                        .append("thuTuUuTiens", "$thuTuUuTiens"))
                .as("cauTraLois");
        ProjectionOperation projectionOperation = project("cauTraLois")
                .and("$_id").as("cauHoi");
        SortOperation sortOperation = sort(Sort.Direction.ASC, "cauHoi.thuTu");

        Aggregation aggregation = newAggregation(
                matchOperation,
                unwindOperation,
                groupOperation,
                groupOperation1,
                groupOperation2,
                projectionOperation,
                sortOperation
        );
        AggregationResults<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep> aggregate = mongoTemplate.aggregate(aggregation,
                KetQuaKhaoSatCongTacVien.class,
                KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep.class);
        List<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep> ketQuaKhaoSatCongTacVienLoaiCauHoiSapXeps = aggregate.getMappedResults();

        // them cac vi tri co so luot chon = 0
        List<KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep> results = new ArrayList<>();
        for (int i = 0; i < ketQuaKhaoSatCongTacVienLoaiCauHoiSapXeps.size(); i++) {
            KetQuaKhaoSatCongTacVienLoaiCauHoiSapXep ketQuaKhaoSat = ketQuaKhaoSatCongTacVienLoaiCauHoiSapXeps.get(i);
            List<CauTraLoi> ketQuas = new ArrayList<>();
            for (int j = 0; j < ketQuaKhaoSat.getCauTraLois().size(); j++) {
                CauTraLoi cauTraLoi = ketQuaKhaoSat.getCauTraLois().get(j);
                List<ThuTuUuTien> thuTuUuTiens = ketQuaKhaoSat.getCauTraLois().get(j).getThuTuUuTiens();
                for (int k = 0; k < ketQuaKhaoSat.getCauHoi().getDanhSachLuaChon().size(); k++) {
                    int finalK = k;
                    if (!thuTuUuTiens.stream().anyMatch(el -> el.getThuTuUuTien() == finalK)) {
                        thuTuUuTiens.add(new ThuTuUuTien(k, 0));
                    }
                }
                cauTraLoi.setThuTuUuTiens(thuTuUuTiens.stream()
                        .sorted(Comparator.comparingInt(ThuTuUuTien::getThuTuUuTien))
                        .collect(Collectors.toList()));
                ketQuas.add(cauTraLoi);
            }
            ketQuaKhaoSat.setCauTraLois(ketQuas.stream()
                    .sorted(Comparator.comparing(CauTraLoi::getDapAn))
                    .collect(Collectors.toList()));
            results.add(ketQuaKhaoSat);
        }
        return results;
    }
}
