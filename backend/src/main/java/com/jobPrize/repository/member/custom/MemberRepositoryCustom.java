package com.jobPrize.repository.member.custom;

import java.util.Optional;

import com.jobPrize.entity.Member;

public interface MemberRepositoryCustom {
	Optional<Member> findWithAllDocumentsById(Long id);
	Optional<Member> findWithProposalsById(Long id);
	Optional<Member> findWithApplicationsById(Long id);
	Optional<Member> findWithPaymentsById(Long id);
	
}
