package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface KetQuaKhaoSatDoanhNghiepRepository extends MongoRepository<KetQuaKhaoSatDoanhNghiep, String> {

    @Query(value = "{$or:[{'cauHoi.cauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'cauHoi.loaiCauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'cauHoi.luaChonToiDa': ?0}" +
            ",{'chuyenThamQuan.$id':?0}"+
            ",{'userKhaoSat.$id':?0}"+
            " ]}"
            , sort = "{'trangThai': 1, 'chuyenThamQuan': 1}")
    Page<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsPaging(String search, Pageable pageable);

    @Query(value = "{$and: [{'trangThai':true}" +
            ",{$or:[{'cauHoi.cauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'cauHoi.loaiCauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'chuyenThamQuan.$id':?0}"+
            ",{'userKhaoSat.$id':?0}"+
            ",{'cauHoi.luaChonToiDa': ?0} ]} ]}"
            , sort = "{ 'chuyenThamQuan': 1, 'userKhaoSat': 1}")
    List<KetQuaKhaoSatDoanhNghiep> getAllKetQuaKhaoSatDoanhNghiepsActive(String search);

    List<KetQuaKhaoSatDoanhNghiep> findKetQuaKhaoSatDoanhNghiepsByUserKhaoSatAndChuyenThamQuan(DoanhNghiep doanhNghiep, ChuyenThamQuan chuyenThamQuan);

}
