package com.jobPrize.companyService.service.Application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.application.ApplicationDetailResponseDto;
import com.jobPrize.companyService.service.Application.api.ApplicationDetailService;
import com.jobPrize.companyService.service.integrated.InterestService;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.Pass;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;
import com.jobPrize.repository.memToCom.pass.PassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultApplicationDetailService implements ApplicationDetailService {

    private final ApplicationRepository applicationRepository;
    private final PassRepository passRepository;
    private final InterestService interestService;

    @Override
    @Transactional(readOnly = true)
    public ApplicationDetailResponseDto getApplicationDetail(Long applicationId) {
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new IllegalStateException("해당 지원서를 찾을 수 없습니다."));
        Pass pass = passRepository.findByApplication(application);

        return ApplicationDetailResponseDto.builder()
                .id(application.getId())
                .name(application.getMember().getUser().getName())  // ✅ User에서 가져옴
                .email(application.getMember().getUser().getEmail())  // ✅ User에서 가져옴
                .birthDate(application.getMember().getUser().getBirthDate())  // ✅ User에서 가져옴
                .gender(application.getMember().getUser().getGenderType())  // ✅ User에서 가져옴
                .hasCareer(!application.getMember().getCareers().isEmpty())
                .job(application.getJobPosting().getJob())
                .phone(application.getMember().getUser().getPhone())  // ✅ User에서 가져옴
                .isPassed(pass != null && pass.isPassed())
                .isInterested(interestService.isApplicationInterested(application.getJobPosting().getCompany().getId(), application.getId()))
                .resumeJson(application.getResumeJson())
                .careerDescriptionJson(application.getCareerDescriptionJson())
                .coverLetterJson(application.getCoverLetterJson())
                .build();
    }
}