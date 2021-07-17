package com.stc.thamquan.feigns;

import com.stc.thamquan.dtos.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/4/21
 * Time      : 01:15
 * Filename  : ScheduleServiceFallback
 */
@Slf4j
@Component
public class ScheduleServiceClientFallback implements ScheduleServiceClient {
    @Override
    public String createEmailSchedule(EmailDto dto) {
        return "null";
    }
}
