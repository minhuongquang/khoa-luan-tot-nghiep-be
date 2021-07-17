package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.ChuyenThamQuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:13
 * Filename  : ChuyenThamQuanRepository
 */
public interface ChuyenThamQuanRepository extends MongoRepository<ChuyenThamQuan, String> {

    @Query(value = "{'dotThamQuan._id': ?0, 'danhSachSinhViens.sinhVienId': ?1}", sort = "{'tenChuyenThamQuan': 1}")
    List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndSinhVienThamGia(String dotThamQuanId, String sinhVienId);

    @Query(value = "{$and:[{'congTacViens.congTacVien.email': ?0}" +
            ",{'congTacViens.trangThai':?1}" +
            ",{'trangThai':?2}" +
            "]}", sort = "{'tenChuyenThamQuan': 1}")
    List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndCongTacVien(String email, Boolean trangThai, String trangThaiChuyen);

    @Query(value = "{'dotThamQuan._id': ?0, 'danhSachGiangVienThamGias': ?1}", sort = "{'tenChuyenThamQuan': 1}")
    List<ChuyenThamQuan> getAllChuyenThamQuanByDotActiveAndGiangVienThamGia(String dotThamQuanId, String giangVienId);

    @Query(value = "{'dotThamQuan._id': ?0, 'thueXeNgoaiTruong':true}", sort = "{'tenChuyenThamQuan':1}")
    List<ChuyenThamQuan> getChuyenThamQuansByThueXeNgoaiTruong(String dotThamQuanId);

    @Query(value = "{'dotThamQuan._id': ?0}", sort = "{'tenChuyenThamQuan':1}")
    List<ChuyenThamQuan> getChuyenThamQuansByDotThamQuan(String dotThamQuanId);

    //    @Query(value = "{ 'congTacViens.0': { $exists: true } }")
    @Query(value = "{ 'dotThamQuan._id': ?0, 'congTacViens.congTacVien.roles': 'ROLE_CONG_TAC_VIEN' }")
    List<ChuyenThamQuan> getChuyenThamQuansCoCTVByDotThamQuan(String dotThamQuanId);

    @Query(value = "{$and: [{'thoiGianKhoiHanh': ?0}, {'congTacViens.congTacVien.email': ?1}, {'congTacViens.trangThai' : true}]  }")
    ChuyenThamQuan getChuyenThamQuanByThoiGianKhoiHanhAndCongTacVien(Date thoiGianKhoiHanh, String email);

    @Query(value = "{$and:[ { 'tenChuyenThamQuan': { $regex: ?1, $options: 'i'} }, { 'congTacViens': [] }, { 'dotThamQuan._id': ?0 }   ]}"
            , sort = "{'trangThai': -1, 'tenChuyenThamQuan': 1}")
    Page<ChuyenThamQuan> getChuyenThamQuanPagingChuaCoCongTacVien(String dotThamQuanId, String search, Pageable pageable);

    @Query(value = "{'trangThai':'DANG_XU_LY'}", sort = "{'createdDate':1,'thoiGianKhoiHanh':1}")
    List<ChuyenThamQuan> getChuyenThamQuanActiveByTrangThai();

    @Query(value = "{$and: [" +
            "{'trangThai':{$regex: ?0, $options: 'i'}}" +
            ",{'thoiGianBatDauThamQuan': {$gte:?1}}" +
            ",{'thoiGianBatDauThamQuan':{$lte:?2}}" +
            ",{'dotThamQuan._id':?3}" +
            "]}"
            , sort = "{'thoiGianBatDauThamQuan':1}")
    List<ChuyenThamQuan> getDuLieuTimeLineGraphic(String trangThai, Date thoiGianBatDau, Date thoiGianKetThuc, String dotThamQuanId);

    @Query(value = "{$and:[ {$or:[{'tenChuyenThamQuan': {$regex: ?0, $options: 'i'}} ]}," +
            "{ 'trangThai' : 'HOAN_TAT'} ]}", sort = "{'tenChuyenThamQuan': 1}")
    List<ChuyenThamQuan> getAllChuyenThamQuansByTrangThaiHoanTat(String search);
}
