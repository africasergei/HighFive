package com.jobPrize.companyService.service.Application.api;

public interface ApplicationActionService {
    void toggleInterestForApplication(Long companyId, Long applicationId);
    void markAsPassed(Long applicationId);
}