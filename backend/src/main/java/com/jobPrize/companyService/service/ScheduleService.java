package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.schedule.ScheduleCreateRequestDto;
import com.jobPrize.companyService.dto.schedule.ScheduleResponseDto;
import com.jobPrize.companyService.dto.schedule.ScheduleUpdateRequestDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.Schedule;
import com.jobPrize.repository.company.company.CompanyRepository;
import com.jobPrize.repository.company.schedule.ScheduleRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleService {
	
	private final ScheduleRepository scheduleRepository;
	private final CompanyRepository companyRepository;
	
	@Transactional(readOnly = true)
	public List<ScheduleResponseDto> getSchedulesByCompany(Long companyId) {
	    List<Schedule> schedules = scheduleRepository.findAllByCompanyId(companyId); // ✅ QueryDSL 메서드 활용

	    return schedules.stream()
	        .map(schedule -> ScheduleResponseDto.builder()
	            .scheduleId(schedule.getId())
	            .companyId(schedule.getCompany() != null ? schedule.getCompany().getId() : null) // ✅ null 체크 추가
	            .title(schedule.getTitle())
	            .date(schedule.getDate())
	            .build())
	        .toList(); 
	}
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

	    scheduleRepository.save(schedule); // ✅ 새로운 스케줄 저장
	}
	@Transactional
	public void updateSchedule(Long companyId, ScheduleUpdateRequestDto requestDto) {
	    Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId())
	        .orElseThrow(() -> new IllegalStateException("해당 스케줄을 찾을 수 없습니다."));

	    // ✅ 기업 ID가 일치하는지 확인 (보안 강화)
	    if (!schedule.getCompany().getId().equals(companyId)) {
	        throw new IllegalStateException("수정 권한이 없습니다.");
	    }

	    // ✅ 값 업데이트
	    schedule.updateSchedule(requestDto.getTitle(), requestDto.getContent(), requestDto.getDate());

	}
}
