package com.stc.thamquan.dtos.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 13:37
 * Filename  : FileDetails
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {
    private byte[] file;

    private String fileName;

    private long fileSize;
}
