package com.stc.thamquan.services.filehuongdan;

import com.stc.thamquan.dtos.filehuongdan.FileHuongDanDto;
import com.stc.thamquan.entities.FileHuongDan;
import com.stc.thamquan.entities.MyFile;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.FileHuongDanRepository;
import com.stc.thamquan.services.myfile.MyFileService;
import com.stc.thamquan.utils.PageUtils;
import com.stc.vietnamstringutils.VietnameseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 6/11/21
 * Time      : 18:08
 * Filename  : FileHuongDanServiceImpl
 */
@Slf4j
@Service
public class FileHuongDanServiceImpl implements FileHuongDanService {

    private final FileHuongDanRepository fileHuongDanRepository;

    private final VietnameseStringUtils vietnameseStringUtils;

    private final MyFileService myFileService;

    public FileHuongDanServiceImpl(FileHuongDanRepository fileHuongDanRepository, VietnameseStringUtils vietnameseStringUtils, MyFileService myFileService) {
        this.fileHuongDanRepository = fileHuongDanRepository;
        this.vietnameseStringUtils = vietnameseStringUtils;
        this.myFileService = myFileService;
    }


    @Override
    public Page<FileHuongDan> getAllFileHuongDanPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return fileHuongDanRepository.getAllFileHuongDansPaging(vietnameseStringUtils.makeSearchRegex(search), pageable);
    }

    @Override
    public List<FileHuongDan> getAllFileHuongDanActive(String search) {
        return fileHuongDanRepository.getAllFileHuongDansActive(vietnameseStringUtils.makeSearchRegex(search));
    }

    @Override
    public FileHuongDan getFileHuongDan(String id) {
        return fileHuongDanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Văn bản/ Biểu mẫu có id %s không tồn tại", id)));
    }

    @Override
    public FileHuongDan getFileHuongDanByIdCore(String id) {
        return fileHuongDanRepository.findFileHuongDanByIdAndTrangThaiTrue(id)
                .orElse(null);
    }

    @Override
    public FileHuongDan createFileHuongDan(FileHuongDanDto dto) {
        FileHuongDan fileHuongDan = new FileHuongDan();

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự file hướng dẫn không được bỏ trống");
        }
        if (!ObjectUtils.isEmpty(dto.getTieuDeEN())) {
            fileHuongDan.setTieuDeEN(dto.getTieuDeEN());
        }
        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException("Tiêu đề file hướng dẫn không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getFileHuongDan())) {
            throw new InvalidException("File hướng dẫn không được bỏ trống");
        }
        fileHuongDan.setThuTu(dto.getThuTu());
        fileHuongDan.setTieuDe(dto.getTieuDe());
        MyFile myFile = myFileService.getFileInfo(dto.getFileHuongDan());
        fileHuongDan.setFileHuongDan(myFile.getId());
        fileHuongDanRepository.save(fileHuongDan);
        return fileHuongDan;
    }

    @Override
    public FileHuongDan updateFileHuongDan(String id, FileHuongDanDto dto) {
        FileHuongDan fileHuongDan = getFileHuongDan(id);

        if (ObjectUtils.isEmpty(dto.getThuTu())) {
            throw new InvalidException("Thứ tự file hướng dẫn không được bỏ trống");
        }
        if (!ObjectUtils.isEmpty(dto.getTieuDeEN())) {
            fileHuongDan.setTieuDeEN(dto.getTieuDeEN());
        }
        if (ObjectUtils.isEmpty(dto.getTieuDe())) {
            throw new InvalidException("Tiêu đề file hướng dẫn không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getFileHuongDan())) {
            throw new InvalidException("File hướng dẫn không được bỏ trống");
        }
        fileHuongDan.setThuTu(dto.getThuTu());
        fileHuongDan.setTieuDe(dto.getTieuDe());
        MyFile myFile = myFileService.getFileInfo(dto.getFileHuongDan());
        fileHuongDan.setFileHuongDan(myFile.getId());
        fileHuongDanRepository.save(fileHuongDan);
        return fileHuongDan;
    }

    @Override
    public FileHuongDan changeStatus(String id) {
        FileHuongDan fileHuongDan = getFileHuongDan(id);
        fileHuongDan.setTrangThai(!fileHuongDan.isTrangThai());
        fileHuongDanRepository.save(fileHuongDan);
        return fileHuongDan;
    }
}
