package com.stc.thamquan.repositories;


import com.stc.thamquan.entities.CauHoiKhaoSatDoanhNghiep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 15:14
 * Filename  : CauHoiKhaoSatDoanhNghiepRepository
 */
public interface CauHoiKhaoSatDoanhNghiepRepository extends MongoRepository<CauHoiKhaoSatDoanhNghiep, String> {

    @Query(value = "{$or:[{'cauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'loaiCauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'luaChonToiDa':{$regex: ?0}}]}"
            , sort = "{'trangThai': -1, 'thuTu': 1}")
    Page<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatsPaging(String search, Pageable pageable);

    @Query(value="{$and: [{'trangThai':true}" +
            ",{$or:[{'cauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'loaiCauHoi': {$regex: ?0, $options: 'i' }}" +
            ",{'luaChonToiDa':{$regex: ?0}}]}]}"
            , sort = "{ 'thuTu': 1, 'createdDate': 1}")
    List<CauHoiKhaoSatDoanhNghiep> getAllCauHoiKhaoSatDoanhNghiepsActive(String search);

}
