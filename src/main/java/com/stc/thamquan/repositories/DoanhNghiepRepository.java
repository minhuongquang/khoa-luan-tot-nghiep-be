package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.DoanhNghiep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 15:01
 * Filename  : DoanhNghiepRepository
 */
public interface DoanhNghiepRepository extends MongoRepository<DoanhNghiep, String> {

    @Query(value = "{$or:[{'hoTen':{$regex: ?0, $options: 'i' }}, " +
            "{'congTy':{$regex: ?0, $options: 'i' }}," +
            "{'maSoThue':{$regex: ?0, $options: 'i' }}, " +
            "{'email':{$regex: ?0, $options: 'i' }}," +
            "]}", sort = "{'trangThai': -1, 'hoTen': 1}")
    Page<DoanhNghiep> getAllDoanhNghiepsPaging(String search, Pageable pageable);

    @Query(value= "{$and:[{$or:[{'hoTen':{$regex: ?0, $options: 'i' }},"+
            "{'congTy':{$regex: ?0, $options: 'i' }},"+
            "{'maSoThue':{$regex: ?0, $options: 'i' }},"+
            "{'email':{$regex: ?0, $options: 'i' }},"+
            "]}, {'trangThai':true}]}", sort = "{'hoTen': 1}")
    List<DoanhNghiep> getAllDoanhNghiepsActive(String search);

    Optional<DoanhNghiep> findByIdAndTrangThaiTrue(String id);

    Optional<DoanhNghiep> findByEmail(String email);

    Optional<DoanhNghiep> findByMaSoThue(String maSoThue);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMaSoThueIgnoreCase(String maSoThue);
}
