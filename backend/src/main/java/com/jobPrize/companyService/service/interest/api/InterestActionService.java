package com.jobPrize.companyService.service.interest.api;

public interface InterestActionService {
    void toggleInterestForMember(Long companyId, Long memberId);
    void toggleInterestForApplication(Long companyId, Long applicationId);
}