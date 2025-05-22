package com.jobPrize.companyService.service.memberFilter.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;
import com.jobPrize.entity.member.Member;

public interface MemberFilterSearchService {
    Page<Member> findAllByCondition(MemberFilterCondition condition, Pageable pageable);
    Page<Member> getDefaultMemberList(Pageable pageable);
}