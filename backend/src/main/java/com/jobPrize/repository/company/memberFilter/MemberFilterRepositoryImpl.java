package com.jobPrize.repository.company.memberFilter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;
import com.jobPrize.entity.member.Member;
import com.jobPrize.entity.member.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberFilterRepositoryImpl implements MemberFilterRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public Page<Member> findAllByCondition(MemberFilterCondition condition, Pageable pageable) {
		QMember member = QMember.member;
	    
	    BooleanBuilder builder = new BooleanBuilder();

	    if (condition.isHasCareer()) { 
	    	builder.and(member.careers.any().isNotNull());	    }

	    if (condition.getEducation() != null) {
	        builder.and(member.educations.any().educationLevel.eq(condition.getEducation()));
	    }
	    if (condition.getAddress() != null && !condition.getAddress().isBlank()) {
	        builder.and(member.user.address.like("%" + condition.getAddress() + "%"));
	    }
	    if (condition.getJob() != null && !condition.getJob().isBlank()) {
	        builder.and(member.careers.any().job.like("%" + condition.getJob() + "%"));
	    }


		List<Member> results = queryFactory
				.selectFrom(member)
				.leftJoin(member.careers).fetchJoin()  
				.where(builder)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		return new PageImpl<>(results, pageable, countMembersByCondition(condition));
	}

	public long countMembersByCondition(MemberFilterCondition condition) {
		QMember member = QMember.member;
		
	    BooleanBuilder builder = new BooleanBuilder();

	    if (condition.isHasCareer()) {
	    	builder.and(member.careers.any().isNotNull());	    }
	    if (condition.getEducation() != null) {
	        builder.and(member.educations.any().educationLevel.eq(condition.getEducation()));
	    }
	    if (condition.getAddress() != null && !condition.getAddress().isBlank()) {
	        builder.and(member.user.address.like("%" + condition.getAddress() + "%"));
	    }
	    if (condition.getJob() != null && !condition.getJob().isBlank()) {
	        builder.and(member.careers.any().job.like("%" + condition.getJob() + "%"));
	    }

	    return Optional.ofNullable(queryFactory
	            .select(member.countDistinct())
	            .from(member)
	            .leftJoin(member.careers)  
	            .where(builder)
	            .fetchOne()).orElse(0L);
	}

}