package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.CongTacVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/4/21
 * Time      : 15:14
 * Filename  : CongTacVienRepository
 */

public interface CongTacVienRepository extends MongoRepository<CongTacVien, String> {
    @Query(value = "{$or:[{ 'email' : { $regex: ?0, $options: 'i' } }, { 'hoTen' : { $regex: ?0, $options: 'i' } }]}"
            , sort = "{'trangThai': -1, 'hoTen': 1 }")
    Page<CongTacVien> getAllCongTacVienPaging(String search, Pageable pageable);

    Optional<CongTacVien> findByEmail(String email);

    boolean existsByMaSV(String maSV);

    @Query(value = "{$and:[ {$or:[{'maSV': {$regex: ?0, $options: 'i'}}, {'hoTen': {$regex: ?0, $options: 'i'}}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'hoTen': 1}")
    List<CongTacVien> getAllCongTacViensActive(String search);

    Optional<CongTacVien> findByIdAndTrangThaiTrue(String id);
}
