package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.DiaDiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : trucntt
 * Date      : 4/13/21
 * Time      : 15:50
 * Filename  : DiaDiemRepository
 */
public interface DiaDiemRepository extends MongoRepository<DiaDiem, String> {

        @Query(value = "{ 'tenDiaDiem' : { $regex: ?0, $options: 'i' } }"
            , sort = "{'trangThai': -1, 'tenDiaDiem': 1 }")
//    @Aggregation(pipeline = {"{ $sort:{\"trangThai\": -1, \"tenDiaDiem\":1}}"})
    Page<DiaDiem> getAllDiaDiemsPaging(String search, Pageable pageable);

    //    @Query(value = "{$and:[{'trangThai' : true}, { 'tenDiaDiem' : { $regex: ?0, $options: 'i' } }]}"
    //            , sort = "{'tenDiaDiem': 1}")
    @Aggregation(pipeline = {"{ $match : { \"tenDiaDiem\" : { $regex: ?0, $options: 'i' } } }", "{ $match : { \"trangThai\" : true } }", "{ $sort:{\"tenDiaDiem\": 1}}"})
    List<DiaDiem> getAllDiaDiemsActive(String search);

    Optional<DiaDiem> findByIdAndTrangThaiTrue(String id);

    boolean existsByTenDiaDiemIgnoreCase(String tenDiaDiem);
}
