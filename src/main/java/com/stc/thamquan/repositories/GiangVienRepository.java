package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.GiangVien;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/21
 * Time      : 3:52 PM
 * Filename  : GiangVienRepository
 */
public interface GiangVienRepository extends MongoRepository<GiangVien, String> {
    Optional<GiangVien> findByEmail(String email);

    Optional<GiangVien> findByIdAndTrangThaiTrue(String id);

    boolean existsByEmail(String email);

    boolean existsByMaGiangVien(String maGiangVien);

    @Query(value = "{$and:[ {$or:[{'maGiangVien': {$regex: ?0, $options: 'i'}}, {'hoTen': {$regex: ?0, $options: 'i'}}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'maSV': 1}")
    List<GiangVien> getAllGiangViensActive(String search);
}
