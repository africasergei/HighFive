package com.jobPrize.companyService.service.memberPool.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;
import com.jobPrize.companyService.service.integrated.InterestService;
import com.jobPrize.companyService.service.memberPool.api.MemberInterestService;
import com.jobPrize.companyService.service.memberPool.api.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultMemberInterestService implements MemberInterestService {
    private final InterestService interestService;
    private final MemberQueryService memberQueryService;

    @Override
    @Transactional
    public void toggleInterestForMember(Long companyId, Long memberId) {
        interestService.toggleInterestForMember(companyId, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberPoolListResponseDto> getInterestedMembers(Long companyId, Pageable pageable) {
        return memberQueryService.fetchMembersByLatestJobPosting(companyId, pageable); // ✅ 수정된 메서드 사용
    }
}