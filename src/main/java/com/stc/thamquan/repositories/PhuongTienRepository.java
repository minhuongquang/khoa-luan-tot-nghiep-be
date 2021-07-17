package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.PhuongTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/12/21
 * Time      : 15:49
 * Filename  : PhuongTienRepository
 */
public interface PhuongTienRepository extends MongoRepository<PhuongTien, String> {

    @Query(value = "{$or:[{ 'tenXe' : { $regex: ?0, $options: 'i' } }, { 'mauXe' : { $regex: ?0, $options: 'i' } }" +
            ", { 'soChoNgoi' : ?0 }]}"
            , sort = "{ 'trangThai': -1}")
    Page<PhuongTien> getAllPhuongTiensPaging(String search, Pageable pageable);

    @Query(value =  "{$and:[ {$or:[" +
            "{ 'bienSoXe' : { $regex: ?0, $options: 'i' } }," +
            " { 'tenXe' : { $regex: ?0, $options: 'i' } }, " +
            "{ 'mauXe' : { $regex: ?0, $options: 'i' }}," +
            "{ 'soChoNgoi' : ?0 } ]}," +
            "{ 'trangThai' : true}" +
            " ]}", sort = "{'bienSoXe': 1}")
    List<PhuongTien> getAllPhuongTiensActive(String search);

    Optional<PhuongTien> findByIdAndTrangThaiTrue(String id);

    Optional<PhuongTien> findByTenXe(String tenPhuongTien);

    boolean existsByBienSoIgnoreCase(String bienSo);
}
