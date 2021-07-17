package com.stc.thamquan.repositories;

import com.stc.thamquan.entities.CauHinhHeThong;
import com.stc.thamquan.entities.MyFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by: IntelliJ IDEA
 * User      : hung
 * Date      : 4/28/21
 * Time      : 10:00
 * Filename  : MyFileRepository
 */
public interface MyFileRepository extends MongoRepository<MyFile, String> {
}
