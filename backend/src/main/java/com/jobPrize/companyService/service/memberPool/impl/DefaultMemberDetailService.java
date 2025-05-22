package com.jobPrize.companyService.service.memberPool.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.memberPool.MemberDetailResponseDto;
import com.jobPrize.companyService.service.memberPool.api.MemberDetailService;
import com.jobPrize.companyService.service.memberPool.api.MemberQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultMemberDetailService implements MemberDetailService {
    private final MemberQueryService memberQueryService;

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(Long companyId, Long memberId) {
        return memberQueryService.getMemberDetail(companyId, memberId); // ✅ 수정된 메서드 사용
    }
}
