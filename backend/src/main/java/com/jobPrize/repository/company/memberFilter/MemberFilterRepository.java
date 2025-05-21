package com.jobPrize.repository.company.memberFilter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.member.Member;

public interface MemberFilterRepository extends JpaRepository<Member, Long>, MemberFilterRepositoryCustom{

}
