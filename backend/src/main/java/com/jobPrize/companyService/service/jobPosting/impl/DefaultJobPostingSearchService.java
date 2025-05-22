package com.jobPrize.companyService.service.jobPosting.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.jobPosting.JobPostingDetailResponseDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingListResponseDto;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;
import com.jobPrize.companyService.service.jobPosting.api.JobPostingSearchService;
import com.jobPrize.companyService.service.jobPostingImage.api.JobPostingImageSearchService;
import com.jobPrize.entity.company.JobPosting;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobPostingSearchService implements JobPostingSearchService {

    private final CompanyJobPostingRepository companyJobPostingRepository;
    private final JobPostingImageSearchService jobPostingImageSearchService;

    @Override
    @Transactional(readOnly = true)
    public JobPostingDetailResponseDto readJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = companyJobPostingRepository.findWithJobPostingImageByJobPostingId(jobPostingId)
                .orElseThrow(() -> new IllegalStateException("해당 채용공고를 찾을 수 없습니다."));
        List<String> imageUrls = jobPostingImageSearchService.getImageUrlsByJobPostingId(jobPostingId);

        return JobPostingDetailResponseDto.builder()
                .companyId(jobPosting.getCompany().getId())
                .jobPostingId(jobPosting.getId())
                .title(jobPosting.getTitle())
                .companyType(jobPosting.getCompanyType())
                .workingHours(jobPosting.getWorkingHours())
                .workLocation(jobPosting.getWorkLocation())
                .job(jobPosting.getJob())
                .careerType(jobPosting.getCareerType())
                .educationLevel(jobPosting.getEducationLevel())
                .salary(jobPosting.getSalary())
                .content(jobPosting.getContent())
                .requirement(jobPosting.getRequirement())
                .imageUrls(imageUrls)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobPostingListResponseDto> getMyJobPostings(Long companyId, Pageable pageable) {
        Page<JobPosting> jobPostings = companyJobPostingRepository.findAllByCompanyId(companyId, pageable);
        return jobPostings.map(this::convertToDto);
    }

    private JobPostingListResponseDto convertToDto(JobPosting jobPosting) {
        return JobPostingListResponseDto.builder()
                .id(jobPosting.getId())
                .companyType(jobPosting.getCompanyType())
                .title(jobPosting.getTitle())
                .job(jobPosting.getJob())
                .workLocation(jobPosting.getWorkLocation())
                .careerType(jobPosting.getCareerType())
                .educationLevel(jobPosting.getEducationLevel())
                .createdDate(jobPosting.getCreatedDate())
                .build();
    }
}