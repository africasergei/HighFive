package com.jobPrize.companyService.service.memberPool.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;

public interface MemberInterestService {
    void toggleInterestForMember(Long companyId, Long memberId);
    Page<MemberPoolListResponseDto> getInterestedMembers(Long companyId, Pageable pageable);
}