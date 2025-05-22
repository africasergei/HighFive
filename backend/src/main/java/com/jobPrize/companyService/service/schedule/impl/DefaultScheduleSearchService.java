package com.jobPrize.companyService.service.schedule.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.schedule.ScheduleResponseDto;
import com.jobPrize.repository.company.schedule.ScheduleRepository;
import com.jobPrize.companyService.service.schedule.api.ScheduleSearchService;
import com.jobPrize.entity.company.Schedule;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultScheduleSearchService implements ScheduleSearchService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedulesByCompany(Long companyId) {
        List<Schedule> schedules = scheduleRepository.findAllByCompanyId(companyId);

        return schedules.stream()
                .map(schedule -> ScheduleResponseDto.builder()
                        .scheduleId(schedule.getId())
                        .companyId(schedule.getCompany() != null ? schedule.getCompany().getId() : null)
                        .title(schedule.getTitle())
                        .date(schedule.getDate())
                        .build())
                .toList();
    }
}