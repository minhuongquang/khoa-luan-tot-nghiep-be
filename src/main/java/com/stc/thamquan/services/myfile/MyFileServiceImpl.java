package com.stc.thamquan.services.myfile;

import com.stc.thamquan.dtos.FileStoreResult;
import com.stc.thamquan.entities.MyFile;
import com.stc.thamquan.exceptions.InvalidException;
import com.stc.thamquan.exceptions.NotFoundException;
import com.stc.thamquan.repositories.MyFileRepository;
import com.stc.thamquan.services.filestore.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by: IntelliJ IDEA
 * User      : hung
 * Date      : 4/28/21
 * Time      : 10:00
 * Filename  : MyFileServiceImpl
 */
@Slf4j
@Service
public class MyFileServiceImpl implements MyFileService {
    private final MyFileRepository myFileRepository;

    private final FileStorageService fileStorageService;

    private final MessageSource messageSource;

    private static final List<String> contentImageTypes = Arrays.asList(
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE);

    public MyFileServiceImpl(MyFileRepository myFileRepository, FileStorageService fileStorageService, MessageSource messageSource) {
        this.myFileRepository = myFileRepository;
        this.fileStorageService = fileStorageService;
        this.messageSource = messageSource;
    }

    @Override
    public MyFile uploadFile(MultipartFile file, String subFolder, Principal principal) throws Exception {
        File temp = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileUtils.copyInputStreamToFile(file.getInputStream(), temp);
        FileStoreResult fileStoreResult = fileStorageService.storeFile(temp, subFolder);
        MyFile myFile = new MyFile();
        myFile.setTenFile(fileStoreResult.getTenFile());
        myFile.setKichThuoc(file.getSize());
        myFile.setLoaiFile(file.getContentType());
        myFile.setDuongDan(fileStoreResult.getDuongDan());
        myFileRepository.save(myFile);
        return myFile;
    }

    @Override
    public MyFile getFileInfo(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return myFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(messageSource.getMessage("error.filenotfound", null, locale), id)));
    }

    @Override
    public Resource viewFile(String id, Principal principal) {
        Locale locale = LocaleContextHolder.getLocale();
        MyFile myFile = getFileInfo(id);
        if (!contentImageTypes.contains(myFile.getLoaiFile()) && !myFile.getLoaiFile().equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new InvalidException(messageSource.getMessage("error.filestypenotsupport", null, locale));
        }
        return fileStorageService.loadFile(myFile.getDuongDan());
    }

    @Override
    public Resource downloadFile(String id, Principal principal) {
        MyFile myFile = getFileInfo(id);
        return fileStorageService.loadFile(myFile.getDuongDan());
    }
}
