package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.ChuyenThamQuan;
import com.stc.thamquan.entities.DotThamQuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:34
 * Filename  : DotThamQuanRepository
 */
public interface DotThamQuanRepository extends MongoRepository<DotThamQuan, String> {

    @Query(value = "{ 'tenDotThamQuan' : { $regex: ?0, $options: 'i' } }"
            , sort = "{ 'trangThai': -1, 'tenDotThamQuan': 1}")
    Page<DotThamQuan> getAllDotThamQuansPaging(String search, Pageable pageable);

    @Query(value = "{$and: [{'trangThai' : true}" +
            ",{$or:[{'tenDotThamQuan':{$regex: ?0, $options: 'i'}}" +
            ",{'tuNgay':{$gte:?0}}" +
            ",{'denNgay':{$lte:?0}}" +
            ",{'namHoc':{$regex: ?0, $options: 'i'}}" +
            ",{'hocKy':{$regex: ?0, $options: 'i'}},]}" +
            "]}", sort = "{'tenDotThamQuan': 1}")
    Page<DotThamQuan> getAllDotThamQuanActive(String search, Pageable pageable);

    Optional<DotThamQuan> findByIdAndTrangThaiTrue(String id);

    Optional<DotThamQuan> findByTrangThaiTrue();

    List<DotThamQuan> getAllByTrangThaiTrue();
}
