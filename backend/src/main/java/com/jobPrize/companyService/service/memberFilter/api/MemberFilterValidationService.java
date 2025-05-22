package com.jobPrize.companyService.service.memberFilter.api;

import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;

public interface MemberFilterValidationService {
    void validateFilterCondition(MemberFilterCondition condition);
}