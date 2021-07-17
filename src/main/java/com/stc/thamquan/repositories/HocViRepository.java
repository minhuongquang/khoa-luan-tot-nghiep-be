package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.HocVi;
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
 * Filename  : HocViRepository
 */
public interface HocViRepository extends MongoRepository<HocVi, String> {
    //Get All Học vị phân trang
    @Query(value = "{$or: [{ 'tenHocVi' : {$regex: ?0, $options: 'i'} }, "
            + "{'tenVietTat': {$regex: ?0, $options: 'i'}} ]}"
            , sort = "{'trangThai': -1, 'tenHocVi': 1}")
    Page<HocVi> getAllHocViPaging(String search, Pageable pageable);


    //Get list học vị active (trang thái true)
    @Query(value = "{$and:[ {$or:[{ 'tenHocVi' : { $regex: ?0, $options: 'i' } }," +
            "{ 'tenVietTat' : { $regex: ?0, $options: 'i' }}]}," +
            "{ 'trangThai' : true} ]}", sort = "{'tenHocHam': 1}")
    List<HocVi> getAllHocVis(String search);

    //Get học vị với id core và trạng thái true
    Optional<HocVi>getByIdAndTrangThaiTrue(String id);


    boolean existsByTenVietTatIgnoreCase(String tenVietTat);
}
