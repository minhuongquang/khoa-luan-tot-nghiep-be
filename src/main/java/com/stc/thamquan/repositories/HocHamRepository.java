package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.HocHam;
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
 * Time      : 3:52 PM
 * Filename  : HocHamRepository
 */
public interface HocHamRepository extends MongoRepository<HocHam, String> {

    @Query(value = "{$or:[{ 'tenHocHam' : { $regex: ?0, $options: 'i' } }" +
            ", { 'tenVietTat' : { $regex: ?0, $options: 'i' }}]}"
            , sort = "{'trangThai': -1, 'tenHocHam': 1}")
    Page<HocHam> getAllHocHamPaging(String search, Pageable pageable);

    @Query(value = "{$and:[ {$or:[{ 'tenHocHam' : { $regex: ?0, $options: 'i' } }, " +
            "{ 'tenVietTat' : { $regex: ?0, $options: 'i' }}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'tenHocHam': 1}")
    List<HocHam> getAllHocHams(String search);

    Optional<HocHam> findByIdAndTrangThaiTrue(String id);

    boolean existsByTenHocHamIgnoreCase(String tenHocHam);

    boolean existsByTenVietTatIgnoreCase(String tenVietTat);
}
