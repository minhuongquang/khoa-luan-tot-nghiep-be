package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.LichSuChuyenThamQuan;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/27/21
 * Time      : 10:22
 * Filename  : LichSuChuyenThamQuanRepository
 */
public interface LichSuChuyenThamQuanRepository extends MongoRepository<LichSuChuyenThamQuan, String> {
}
