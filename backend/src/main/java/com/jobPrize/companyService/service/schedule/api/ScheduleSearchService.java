package com.jobPrize.companyService.service.schedule.api;

import java.util.List;
import com.jobPrize.companyService.dto.schedule.ScheduleResponseDto;

public interface ScheduleSearchService {
    List<ScheduleResponseDto> getSchedulesByCompany(Long companyId);
}