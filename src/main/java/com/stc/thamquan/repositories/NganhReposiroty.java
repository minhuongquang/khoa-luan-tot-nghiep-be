package com.stc.thamquan.repositories;


import com.stc.thamquan.entities.Nganh;
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
 * Filename  : NganhReposiroty
 */
public interface NganhReposiroty extends MongoRepository<Nganh, String> {

    @Query(value = "{$or:[{ 'tenNganh' : { $regex: ?0, $options: 'i' } }, { 'maNganh' : { $regex: ?0, $options: 'i' } }]}"
            , sort = "{'trangThai': -1, 'maNganh': 1, 'tenNganh': 1}")
    Page<Nganh> getAllNganhPaging(String search, Pageable pageable);

    @Query(value = "{$and:[ {$or:[{'maNganh': {$regex: ?0, $options: 'i'}}, {'tenNganh': {$regex: ?0, $options: 'i'}}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'tenNganh': 1}")
    List<Nganh> getAllNganhActive(String search);

    @Query(value = "{$and:[ {'khoa._id': ?0}, {'trangThai': true} ]}", sort = "{'thuTu': 1}")
    List<Nganh> getAllNganhByKhoaId(String khoaId);

    boolean existsByMaNganhIgnoreCase(String maNganh);

    Optional<Nganh> findByIdAndTrangThaiTrue(String id);

    Optional<Nganh> findByMaNganh(String maNganh);

}
