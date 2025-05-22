package com.jobPrize.companyService.service.Application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.application.ApplicationListResponseDto;
import com.jobPrize.companyService.service.Application.api.ApplicationSearchService;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultApplicationSearchService implements ApplicationSearchService {

    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationListResponseDto> getApplicantsByJobPostingId(Long jobPostingId, Pageable pageable) {
        Page<Application> applicationPage = applicationRepository.findAllByJobPostingId(jobPostingId, pageable);
        return applicationPage.map(this::convertToDto);
    }

    private ApplicationListResponseDto convertToDto(Application application) {
        return ApplicationListResponseDto.builder()
                .memberId(application.getMember().getId())
                .jobPostingId(application.getJobPosting().getId())
                .companyId(application.getJobPosting().getCompany().getId())
                .name(application.getMember().getUser().getName())
                .gender(application.getMember().getUser().getGenderType())
                .birthDate(application.getMember().getUser().getBirthDate())
                .hasCareer(!application.getMember().getCareers().isEmpty())
                .job(application.getJobPosting().getJob())
                .educationLevel(application.getMember().getEducations().isEmpty() ? null : application.getMember().getEducations().get(0).getEducationLevel())
                .createdDate(application.getCreatedDate())
                .build();
    }
}