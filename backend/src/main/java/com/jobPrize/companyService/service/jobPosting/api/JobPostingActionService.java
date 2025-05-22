package com.jobPrize.companyService.service.jobPosting.api;

import com.jobPrize.companyService.dto.jobPosting.JobPostingCreateDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingUpdateDto;
import com.jobPrize.entity.company.JobPosting;

public interface JobPostingActionService {
    JobPosting createJobPosting(Long userId, JobPostingCreateDto dto);
    JobPosting updateJobPosting(Long companyId, JobPostingUpdateDto dto);
    void deleteJobPosting(Long companyId, Long jobPostingId);
}