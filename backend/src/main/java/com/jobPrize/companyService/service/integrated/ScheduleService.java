package com.jobPrize.companyService.service.integrated;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.schedule.ScheduleCreateRequestDto;
import com.jobPrize.companyService.dto.schedule.ScheduleResponseDto;
import com.jobPrize.companyService.dto.schedule.ScheduleUpdateRequestDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.Schedule;
import com.jobPrize.repository.company.company.CompanyRepository;
import com.jobPrize.repository.company.schedule.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CompanyRepository companyRepository;

    // ✅ 기업별 스케줄 조회
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

    // ✅ 신규 스케줄 생성
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

    // ✅ 기존 스케줄 수정
    @Transactional
    public void updateSchedule(Long companyId, ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
            .orElseThrow(() -> new IllegalStateException("해당 스케줄을 찾을 수 없습니다."));

        // ✅ 기업 ID가 일치하는지 확인
        if (!schedule.getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        schedule.updateSchedule(requestDto.getTitle(), requestDto.getContent(), requestDto.getDate());
    }
}