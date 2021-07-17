package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.FileHuongDan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileHuongDanRepository  extends MongoRepository<FileHuongDan, String> {
    @Query(value = "{$or:[{'tieuDe': {$regex: ?0, $options: 'i' }}" +
            ",{'tieuDeEN': {$regex: ?0, $options: 'i' }}" +
            ",{'thuTu':{$regex: ?0}}]}"
            , sort = "{'trangThai': -1, 'thuTu': 1}")
    Page<FileHuongDan> getAllFileHuongDansPaging(String search, Pageable pageable);

    @Query(value="{$and: [{'trangThai':true}" +
            ",{$or:[{'tieuDe': {$regex: ?0, $options: 'i' }}" +
            ",{'tieuDeEN': {$regex: ?0, $options: 'i' }}" +
            ",{'thuTu':{$regex: ?0}}]}]}"
            , sort = "{ 'thuTu': 1, 'createdDate': 1}")
    List<FileHuongDan> getAllFileHuongDansActive(String search);

    Optional<FileHuongDan> findFileHuongDanByIdAndTrangThaiTrue(String id);

}
