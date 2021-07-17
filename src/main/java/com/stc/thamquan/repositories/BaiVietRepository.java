package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.BaiViet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/6/21
 * Time      : 15:25
 * Filename  : BaiVietRepository
 */
public interface BaiVietRepository extends MongoRepository<BaiViet, String> {
}
