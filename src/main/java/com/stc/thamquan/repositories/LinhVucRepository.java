package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.LinhVuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 15:36
 * Filename  : LinhVucRepository
 */
public interface LinhVucRepository extends MongoRepository<LinhVuc, String> {
    @Query(value = "{ $or:[ { 'maLinhVuc' : { $regex: ?0, $options: 'i' }}, { 'tenLinhVuc' : { $regex: ?0, $options: 'i' }} ] }"
            , sort = "{'trangThai': -1, 'maLinhVuc': 1}")
    Page<LinhVuc> getAllLinhVucPaging(String search, Pageable pageable);

    @Query(value = "{ $and:[ " +
            "{$or:[{ 'maLinhVuc' : { $regex: ?0, $options: 'i' } }, { 'tenLinhVuc' : { $regex: ?0, $options: 'i' } } ]}," +
            "{ 'trangThai' : true} ]}", sort = "{'maLinhVuc': 1}")
    List<LinhVuc> getLinhVucs(String search);

    Optional<LinhVuc> findByIdAndTrangThaiTrue(String id);

    Optional<LinhVuc> findByTenLinhVuc(String tenLinhVuc);

    Optional<LinhVuc> findByMaLinhVuc(String id);

    boolean existsByMaLinhVucIgnoreCase(String maLinhVuc);
}
