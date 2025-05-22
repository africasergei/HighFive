package com.jobPrize.companyService.service.Application.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.companyService.dto.application.ApplicationListResponseDto;

public interface ApplicationSearchService {
    Page<ApplicationListResponseDto> getApplicantsByJobPostingId(Long jobPostingId, Pageable pageable);
}