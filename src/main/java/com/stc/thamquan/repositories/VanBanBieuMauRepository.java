package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.VanBanBieuMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VanBanBieuMauRepository extends MongoRepository<VanBanBieuMau, String> {

    @Query(value = "{$or: [{'tenVanBanBieuMau': {$regex: ?0, $options: 'i'}}" +
            ",{'tenVanBanBieuMauEn': {$regex: ?0, $options: 'i'}}]}"
            , sort = "{'trangThai':-1, 'tenVanBanBieuMau':1}")
    Page<VanBanBieuMau> getAllVanBanBieuMauPaging(String search, Pageable pageable);

    @Query(value = "{$and:[{'trangThai':true}" +
            ",{$or: [{'tenVanBanBieuMau': {$regex: ?0, $options: 'i'}}" +
            ",{'tenVanBanBieuMauEn': {$regex: ?0, $options: 'i'}}]}" +
            "]}"
            , sort = "{'trangThai':-1, 'tenVanBanBieuMau':1}")
    List<VanBanBieuMau> getAllVanBanBieuMauActive(String search);

    Optional<VanBanBieuMau> findVanBanBieuMauByIdAndTrangThaiTrue(String id);

}
