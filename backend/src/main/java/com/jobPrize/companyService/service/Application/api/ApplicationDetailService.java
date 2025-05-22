package com.jobPrize.companyService.service.Application.api;

import com.jobPrize.companyService.dto.application.ApplicationDetailResponseDto;

public interface ApplicationDetailService {
	ApplicationDetailResponseDto getApplicationDetail(Long applicationId);
}
