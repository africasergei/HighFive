package com.jobPrize.repository.company.memberFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;
import com.jobPrize.entity.member.Member;

public interface MemberFilterRepositoryCustom {
	Page<Member> findAllByCondition(MemberFilterCondition condition, Pageable pageable);
}
