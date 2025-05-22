package com.jobPrize.companyService.service.memberFilter.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jobPrize.companyService.service.memberFilter.api.MemberFilterSearchService;
import com.jobPrize.companyService.service.memberFilter.api.MemberFilterValidationService;
import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.company.memberFilter.MemberFilterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultMemberFilterSearchService implements MemberFilterSearchService {

    private final MemberFilterRepository memberFilterRepository;
    private final MemberFilterValidationService validationService; // ✅ 필터 검증 서비스 사용

    @Override
    public Page<Member> findAllByCondition(MemberFilterCondition condition, Pageable pageable) {
        validationService.validateFilterCondition(condition); // ✅ 필터 조건 검증
        return memberFilterRepository.findAllByCondition(condition, pageable);
    }

    @Override
    public Page<Member> getDefaultMemberList(Pageable pageable) {
        return memberFilterRepository.findAll(pageable);
    }
}