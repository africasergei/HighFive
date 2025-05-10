package com.jobPrize.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.Member;
import com.jobPrize.repository.member.custom.MemberRepositoryCustom;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	Optional<Member> findByEmail(String email);
}
