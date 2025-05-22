package com.jobPrize.companyService.service.memberPool.api;

import com.jobPrize.companyService.dto.memberPool.MemberDetailResponseDto;

public interface MemberDetailService {
    MemberDetailResponseDto getMemberDetail(Long companyId, Long memberId);
}