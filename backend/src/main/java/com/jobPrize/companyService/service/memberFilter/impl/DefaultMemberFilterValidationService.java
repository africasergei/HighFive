package com.jobPrize.companyService.service.memberFilter.impl;

import org.springframework.stereotype.Service;
import com.jobPrize.companyService.service.memberFilter.api.MemberFilterValidationService;
import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;

@Service
public class DefaultMemberFilterValidationService implements MemberFilterValidationService {

    @Override
    public void validateFilterCondition(MemberFilterCondition condition) {
        boolean isValid = condition.isHasCareer() || condition.getEducation() != null ||
                (condition.getAddress() != null && !condition.getAddress().isBlank()) ||
                (condition.getJob() != null && !condition.getJob().isBlank());

        if (!isValid) {
            throw new IllegalArgumentException("최소 하나 이상의 필터 조건을 선택해야 합니다.");
        }
    }
}