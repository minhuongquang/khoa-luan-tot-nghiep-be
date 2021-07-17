package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends MongoRepository<Banner, String> {

    @Query(value = "{$or:[ { 'tieuDe': {$regex: ?0, $options: 'i' }}, { 'fileBanner': {$regex: ?0, $options: 'i' }} ]}", sort = "{'trangThai': -1, 'thuTu': 1}")
    Page<Banner> getAllBannerPaging(String search, Pageable pageable);

    @Query(value = "{$and: [ {$or: [{ 'tieuDe': {$regex: ?0, $options: 'i' }}, { 'fileBanner': {$regex: ?0, $options: 'i' }} ] }, { 'trangThai': true } ]}", sort = "{'thuTu': 1}")
    List<Banner> getAllBannerActive(String search);

    Optional<Banner> findByIdAndTrangThaiTrue(String id);
}
