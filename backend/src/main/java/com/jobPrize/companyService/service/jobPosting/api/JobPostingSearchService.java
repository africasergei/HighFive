package com.jobPrize.companyService.service.jobPosting.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jobPrize.companyService.dto.jobPosting.JobPostingDetailResponseDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingListResponseDto;

public interface JobPostingSearchService {
    JobPostingDetailResponseDto readJobPostingDetail(Long jobPostingId);
    Page<JobPostingListResponseDto> getMyJobPostings(Long companyId, Pageable pageable);
}