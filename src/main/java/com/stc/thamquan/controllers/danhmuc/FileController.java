package com.stc.thamquan.controllers.danhmuc;

import com.stc.thamquan.dtos.myfile.MyFileDto;
import com.stc.thamquan.entities.MyFile;
import com.stc.thamquan.services.myfile.MyFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
/**
 * Created by: IntelliJ IDEA
 * User      : truc
 * Date      : 05/29/21
 * Time      : 14:31
 * Filename  : MyFileController
 */
@RestController
@RequestMapping("/rest/file")
public class FileController {
    private final MyFileService myFileService;

    public FileController(MyFileService myFileService) {
        this.myFileService = myFileService;
    }


    @PostMapping
    public ResponseEntity<MyFile> uploadFile(@Valid @RequestParam MultipartFile file,
                                             @RequestParam(name = "subFolder", defaultValue = "DT") String subFolder,
                                             Principal principal) throws Exception {
        return new ResponseEntity<>(myFileService.uploadFile(file, subFolder.toLowerCase(), principal), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyFile> getFileInfo(@PathVariable("id") String id) {
        return new ResponseEntity<>(myFileService.getFileInfo(id), HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable("id") String id, Principal principal) {
        return new ResponseEntity<>(myFileService.viewFile(id, principal), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") String id, Principal principal) {
        return new ResponseEntity<>(myFileService.downloadFile(id, principal), HttpStatus.OK);
    }
}
