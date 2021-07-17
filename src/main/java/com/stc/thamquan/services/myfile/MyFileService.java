package com.stc.thamquan.services.myfile;

import com.stc.thamquan.dtos.myfile.MyFileDto;
import com.stc.thamquan.entities.MyFile;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 * Created by: IntelliJ IDEA
 * User      : hung
 * Date      : 4/28/21
 * Time      : 10:00
 * Filename  : MyFileService
 */
public interface MyFileService {
    MyFile uploadFile(MultipartFile file, String subFolder, Principal principal) throws Exception;

    MyFile getFileInfo(String id);

    Resource viewFile(String id, Principal principal);

    Resource downloadFile(String id, Principal principal);

}
