package com.stc.thamquan.services.filehuongdan;

import com.stc.thamquan.dtos.filehuongdan.FileHuongDanDto;
import com.stc.thamquan.entities.FileHuongDan;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FileHuongDanService {
    Page<FileHuongDan> getAllFileHuongDanPaging(String search, int page, int size, String sort, String column);

    List<FileHuongDan> getAllFileHuongDanActive(String search);

    FileHuongDan getFileHuongDan(String id);

    FileHuongDan getFileHuongDanByIdCore(String id);

    FileHuongDan createFileHuongDan(FileHuongDanDto dto);

    FileHuongDan updateFileHuongDan(String id, FileHuongDanDto dto);

    FileHuongDan changeStatus(String id);
}
