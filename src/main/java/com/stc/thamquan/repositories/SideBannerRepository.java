package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.SideBanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SideBannerRepository extends MongoRepository<SideBanner, String> {

    @Query(value = "{$or:[ { 'tieuDe': {$regex: ?0, $options: 'i' }}, { 'fileSideBanner': {$regex: ?0, $options: 'i' }} ]}", sort = "{'trangThai': -1, 'thuTu': 1}")
    Page<SideBanner> getAllSideBannerPaging(String search, Pageable pageable);

    @Query(value = "{$and: [ {$or: [{ 'tieuDe': {$regex: ?0, $options: 'i' }}, { 'fileSideBanner': {$regex: ?0, $options: 'i' }} ] }, { 'trangThai': true } ]}", sort = "{'thuTu': 1}")
    List<SideBanner> getAllSideBannerActive(String search);

    Optional<SideBanner> findByIdAndTrangThaiTrue(String id);
}
