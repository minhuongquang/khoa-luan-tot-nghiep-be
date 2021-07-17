package com.stc.thamquan.feigns;

import com.stc.thamquan.dtos.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 5/4/21
 * Time      : 01:14
 * Filename  : ScheduleService
 */
@FeignClient(name = "schedule-service", fallbackFactory = ScheduleServiceClientFallback.class,
url = "http://35.197.145.110:8080/schedule-service")
public interface ScheduleServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/core/email-schedule")
    String createEmailSchedule(@Valid @RequestBody EmailDto dto);
}
