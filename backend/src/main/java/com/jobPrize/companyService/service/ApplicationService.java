package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.application.ApplicationDetailResponseDto;
import com.jobPrize.companyService.dto.application.ApplicationListResponseDto;
import com.jobPrize.companyService.dto.pass.PassListResponseDto;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.Pass;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;
import com.jobPrize.repository.memToCom.pass.PassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InterestService interestService;
    private final PassRepository passRepository;
    
    @Transactional(readOnly = true) 
    public Page<ApplicationListResponseDto> getApplicantsByJobPostingId(Long jobPostingId, Pageable pageable) {
       
        Page<Application> applicationPage = applicationRepository.findAllByJobPostingId(jobPostingId, pageable);

        return applicationPage.map(this::convertToDto);
    }

    @Transactional 
    public void toggleInterestForApplication(String token, Long applicationId) {
        interestService.toggleInterestForApplication(token, applicationId);
    }

    
    private ApplicationListResponseDto convertToDto(Application application) {
        Member member = application.getMember();
        List<Education> educations = member.getEducations();
        boolean isInterested = interestService.isApplicationInterested(application.getJobPosting().getCompany().getId(), application.getId());

        return ApplicationListResponseDto.builder()
                .memberId(member.getId())
                .jobPostingId(application.getJobPosting().getId())
                .companyId(application.getJobPosting().getCompany().getId())
                .name(member.getUser().getName()) // 
                .gender(member.getUser().getGenderType()) 
                .birthDate(member.getUser().getBirthDate())
                .hasCareer(!member.getCareers().isEmpty()) 
                .job(application.getJobPosting().getJob())
                .educationLevel(!educations.isEmpty() ? educations.get(0).getEducationLevel() : null) // ✅ 빈 리스트 방어 코드 유지
                .createdDate(application.getCreatedDate())
                .isInterested(isInterested) 
                .build();
    }
    @Transactional(readOnly = true)
    public ApplicationDetailResponseDto getApplicationDetail(Long applicationId) {
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new IllegalStateException("해당 지원서를 찾을 수 없습니다."));

        Pass pass = passRepository.findByApplication(application); 

        return ApplicationDetailResponseDto.builder()
                .id(application.getId())
                .name(application.getMember().getUser().getName())
                .email(application.getMember().getUser().getEmail())
                .birthDate(application.getMember().getUser().getBirthDate())
                .gender(application.getMember().getUser().getGenderType())
                .hasCareer(!application.getMember().getCareers().isEmpty())
                .job(application.getJobPosting().getJob())
                .phone(application.getMember().getUser().getPhone())
                .isPassed(pass != null && pass.isPassed()) 
                .isInterested(interestService.isApplicationInterested(application.getJobPosting().getCompany().getId(), application.getId()))
                .resumeJson(application.getResumeJson())
                .careerDescriptionJson(application.getCareerDescriptionJson())
                .coverLetterJson(application.getCoverLetterJson())
                .build();
    }
    @Transactional(readOnly = true)
    public Page<PassListResponseDto> getPassedApplicants(Long companyId, Pageable pageable) {
        Page<Pass> passedApplications = passRepository.findPassedApplicantsByCompanyId(companyId, pageable);

        return passedApplications.map(pass -> {
            Application application = pass.getApplication();
            Member member = application.getMember();
            List<Education> educations = member.getEducations();

            return PassListResponseDto.builder()
                .applicationId(application.getId()) 
                .memberId(member.getId())
                .name(member.getUser().getName())
                .gender(member.getUser().getGenderType())
                .birthDate(member.getUser().getBirthDate())
                .hasCareer(!member.getCareers().isEmpty())
                .job(application.getJobPosting().getJob()) 
                .educationLevel(!educations.isEmpty() ? educations.get(0).getEducationLevel() : null) 
                .passDate(pass.getCreatedDate()) // ✅ 합격 날짜 추가
                .title(application.getJobPosting().getTitle())
                .isPassed(true) // ✅ 합격 상태 고정
                .build();
        });
    }
    @Transactional
    public void markAsPassed(Long applicationId) {
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new IllegalStateException("지원서를 찾을 수 없습니다."));
        
        Pass pass = passRepository.findByApplication(application);
        
        if (pass == null) {
            pass = new Pass(application); 
        }

        pass.markAsPassed(); // ✅ 합격 상태 변경
        passRepository.save(pass); // ✅ DB에 저장
    }
}