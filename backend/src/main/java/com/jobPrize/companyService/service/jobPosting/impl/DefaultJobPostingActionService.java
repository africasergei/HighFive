package com.jobPrize.companyService.service.jobPosting.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.jobPosting.JobPostingCreateDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingUpdateDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.common.User;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;
import com.jobPrize.repository.common.user.UserRepository;
import com.jobPrize.companyService.service.jobPosting.api.JobPostingActionService;
import com.jobPrize.companyService.service.jobPostingImage.api.JobPostingImageActionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobPostingActionService implements JobPostingActionService {

    private final CompanyJobPostingRepository companyJobPostingRepository;
    private final JobPostingImageActionService jobPostingImageActionService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public JobPosting createJobPosting(Long userId, JobPostingCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));
        Company company = user.getCompany();
        if (company == null) {
            throw new IllegalStateException("기업 정보가 없습니다.");
        }

        JobPosting jobPosting = JobPosting.builder()
                .company(company)
                .title(dto.getTitle())
                .workingHours(dto.getWorkingHours())
                .workLocation(dto.getWorkLocation())
                .job(dto.getJob())
                .careerType(dto.getCareerType())
                .educationLevel(dto.getEducationLevel())
                .salary(dto.getSalary())
                .content(dto.getContent())
                .requirement(dto.getRequirement())
                .build();

        return companyJobPostingRepository.save(jobPosting);
    }

    @Override
    @Transactional
    public JobPosting updateJobPosting(Long companyId, JobPostingUpdateDto dto) {
        JobPosting jobPosting = companyJobPostingRepository.findById(dto.getJobpostingId())
                .orElseThrow(() -> new IllegalStateException("해당 채용공고를 찾을 수 없습니다."));
        jobPosting.updateJobPostingInfo(
                dto.getTitle(),
                dto.getContent(),
                dto.getJob(),
                dto.getWorkingHours(),
                dto.getWorkLocation(),
                dto.getCareerType(),
                dto.getEducationLevel(),
                dto.getSalary(),
                dto.getRequirement()
        );
        return companyJobPostingRepository.save(jobPosting);
    }

    @Override
    public void deleteJobPosting(Long companyId, Long jobPostingId) {
        companyJobPostingRepository.deleteById(jobPostingId);
    }
}