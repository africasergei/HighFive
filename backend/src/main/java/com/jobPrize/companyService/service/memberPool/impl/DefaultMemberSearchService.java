package com.jobPrize.companyService.service.memberPool.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;
import com.jobPrize.companyService.service.memberPool.api.MemberQueryService;
import com.jobPrize.companyService.service.memberPool.api.MemberSearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultMemberSearchService implements MemberSearchService {
    private final MemberQueryService memberQueryService;

    @Override
    @Transactional(readOnly = true)
    public Page<MemberPoolListResponseDto> searchMembers(Long companyId, Pageable pageable) {
        return memberQueryService.fetchMembersByLatestJobPosting(companyId, pageable); // ✅ 수정된 메서드 사용
    }
}