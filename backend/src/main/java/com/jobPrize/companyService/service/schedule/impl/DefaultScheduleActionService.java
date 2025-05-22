package com.jobPrize.companyService.service.schedule.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.schedule.ScheduleCreateRequestDto;
import com.jobPrize.companyService.dto.schedule.ScheduleUpdateRequestDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.Schedule;
import com.jobPrize.repository.company.company.CompanyRepository;
import com.jobPrize.repository.company.schedule.ScheduleRepository;
import com.jobPrize.companyService.service.schedule.api.ScheduleActionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultScheduleActionService implements ScheduleActionService {

    private final ScheduleRepository scheduleRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public void createSchedule(Long companyId, ScheduleCreateRequestDto requestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException("해당 기업을 찾을 수 없습니다."));

        Schedule schedule = Schedule.builder()
                .company(company)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .date(requestDto.getDate())
                .build();

        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void updateSchedule(Long companyId, ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new IllegalStateException("해당 스케줄을 찾을 수 없습니다."));

        if (!schedule.getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        schedule.updateSchedule(requestDto.getTitle(), requestDto.getContent(), requestDto.getDate());
    }
}