package com.stc.thamquan.dtos;

import com.stc.thamquan.dtos.kafka.FileEmail;
import com.stc.thamquan.dtos.kafka.TextEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/4/21
 * Time      : 01:17
 * Filename  : EmailDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private Date startDate;

    private String jobName;

    private TextEmail textEmail;

    private FileEmail fileEmail;
}