package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.Khoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/21
 * Time      : 3:53 PM
 * Filename  : KhoaRepository
 */
public interface KhoaRepository extends MongoRepository<Khoa, String> {
    @Query(value = "{$or: [{ 'maKhoa' : {$regex: ?0, $options: 'i'} }, "
            + "{ 'tenKhoa' : {$regex: ?0, $options: 'i'}}]}"
            , sort = "{'trangThai': -1, 'maKhoa': 1}")
    Page<Khoa> getAllKhoaPaging(String search, Pageable pageable);

    @Query(value = "{$and:[ {$or:[{ 'maKhoa' : { $regex: ?0, $options: 'i' } }, { 'tenKhoa' : { $regex: ?0, $options: 'i' }}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'maKhoa': 1}")
    List<Khoa> getAllKhoas(String search);

    boolean existsByMaKhoaIgnoreCase(String maKhoa);

    Optional<Khoa>getByIdAndTrangThaiTrue(String id);
}
