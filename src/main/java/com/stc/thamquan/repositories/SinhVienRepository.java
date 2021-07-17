package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.SinhVien;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 15:00N
 * Filename  : SinhVienRepository
 */
public interface SinhVienRepository extends MongoRepository<SinhVien, String> {

    Optional<SinhVien> findByEmail(String email);

    Optional<SinhVien> findByMaSV(String maSV);

    Optional<SinhVien> findByIdAndTrangThaiTrue(String id);

    boolean existsByMaSV(String maSV);

    @Query(value = "{$and:[ {$or:[{ 'maSV' : { $regex: ?0, $options: 'i' } }" +
            ", { 'hoTen' : { $regex: ?0, $options: 'i' }}" +
            ",{ 'lop' :{ $regex: ?0, $options: 'i' } }]}," +
            "{ 'trangThai' : true} ]}", sort = "{'hoTen': 1}")
    List<SinhVien> getAllSinhViensActive(String search);
}
