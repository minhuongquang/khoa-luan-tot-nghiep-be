package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:14
 * Filename  : TaiKhoanRepository
 */
public interface TaiKhoanRepository extends MongoRepository<TaiKhoan, String> {

    @Query(value = "{$or:[{ 'email' : { $regex: ?0, $options: 'i' } }, { 'hoTen' : { $regex: ?0, $options: 'i' } }]}"
            , sort = "{'trangThai': -1, 'hoTen': 1 }")
    Page<TaiKhoan> getAllTaiKhoanPaging(String search, Pageable pageable);

    Optional<TaiKhoan> findByEmail(String email);

    boolean existsByEmail(String email);
}
