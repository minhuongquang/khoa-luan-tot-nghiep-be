package com.stc.thamquan.dtos.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 13:37
 * Filename  : FileEmail
 */
@Getter
@Setter
@NoArgsConstructor
public class FileEmail {
    private String clientId;

    private String emailSender;

    private String passWordSender;

    private List<String> sendTos = new ArrayList<>();

    private String subject;

    private String message;

    private List<FileDetails> files = new ArrayList<>();

    public FileEmail(String clientId,
                     String emailSender,
                     String passWordSender,
                     List<String> sendTos,
                     String subject,
                     String message,
                     List<File> files) {
        this.clientId = clientId;
        this.emailSender = emailSender;
        this.passWordSender = passWordSender;
        this.sendTos = sendTos;
        this.subject = subject;
        this.message = message;
        if (files != null && files.size() != 0)
            this.files = getFileDetailsFromFiles(files);
    }

    public FileEmail(List<String> sendTos,
                     String subject,
                     String message,
                     List<MultipartFile> files) {
        this.sendTos = sendTos;
        this.subject = subject;
        this.message = message;
        if (files != null && files.size() != 0)
            this.files = getFileDetailsFromMulttipartFiles(files);
    }

    private List<FileDetails> getFileDetailsFromFiles(List<File> files) {
        return files.parallelStream().map(x -> {
            FileDetails fileDetails = new FileDetails();
            fileDetails.setFileName(x.getName());
            try {
                fileDetails.setFile(IOUtils.toByteArray(new FileInputStream(x)));
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                //e.printStackTrace();
            }
            fileDetails.setFileSize(x.length());
            return fileDetails;
        }).collect(Collectors.toList());
    }

    private List<FileDetails> getFileDetailsFromMulttipartFiles(List<MultipartFile> files) {
        return files.parallelStream().map(x -> {
            FileDetails fileDetails = new FileDetails();
            fileDetails.setFileName(x.getOriginalFilename());
            try {
                fileDetails.setFile(IOUtils.toByteArray(x.getInputStream()));
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                //e.printStackTrace();
            }
            fileDetails.setFileSize(x.getSize());
            return fileDetails;
        }).collect(Collectors.toList());
    }
}
