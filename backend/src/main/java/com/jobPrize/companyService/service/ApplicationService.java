package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.application.ApplicationListResponseDto;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public Page<ApplicationListResponseDto> getApplicantsByJobPostingId(Long jobPostingId, Pageable pageable) {
        // ✅ 특정 채용 공고 ID 기준으로 지원자 목록 조회
        Page<Application> applicationPage = applicationRepository.findAllByJobPostingId(jobPostingId, pageable);

        // ✅ DTO 변환 후 반환
        return applicationPage.map(this::convertToDto);
    }

    private ApplicationListResponseDto convertToDto(Application application) {
        Member member = application.getMember(); 
        List<Education> educations = member.getEducations();
        return ApplicationListResponseDto.builder()
                .memberId(member.getId())
                .jobPostingId(application.getJobPosting().getId()) 
                .companyId(application.getJobPosting().getCompany().getId()) 
                .name(member.getUser().getName()) // ✅ 지원자 이름
                .gender(member.getUser().getGenderType()) // ✅ 성별
                .age(member.getUser().getBirthDate()) // ✅ 나이
                .hasCareer(!member.getCareers().isEmpty()) // ✅ 경력 여부
                .job(application.getJobPosting().getJob())
                .educationLevel(educations.isEmpty() ? null : educations.get(0).getEducationLevel())
                .createdDate(application.getCreatedDate()) // ✅ 지원한 날짜
                .isInterested(application.isInterested()) // ✅ 관심 여부
                .build();
    }
}