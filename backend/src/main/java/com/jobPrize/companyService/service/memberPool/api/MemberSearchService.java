package com.jobPrize.companyService.service.memberPool.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;

public interface MemberSearchService {
    Page<MemberPoolListResponseDto> searchMembers(Long companyId, Pageable pageable);
}