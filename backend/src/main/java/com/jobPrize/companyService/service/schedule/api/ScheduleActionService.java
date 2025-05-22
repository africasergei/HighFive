package com.jobPrize.companyService.service.schedule.api;

import com.jobPrize.companyService.dto.schedule.ScheduleCreateRequestDto;
import com.jobPrize.companyService.dto.schedule.ScheduleUpdateRequestDto;

public interface ScheduleActionService {
    void createSchedule(Long companyId, ScheduleCreateRequestDto requestDto);
    void updateSchedule(Long companyId, ScheduleUpdateRequestDto requestDto);
}