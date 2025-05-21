package com.jobPrize.repository.memToCom.interest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.entity.memToCom.QInterest;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Interest> findAllByCompanyId(Long id, Pageable pageable, boolean isMemberInterest ) {
		QInterest interest = QInterest.interest;
		
		List<Interest> results = queryFactory
				.selectFrom(interest)
				.join(interest.company).fetchJoin()
				.where(interest.company.id.eq(id)
				        .and(isMemberInterest ? interest.member.isNotNull() : interest.application.isNotNull())) 
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		
		return new PageImpl<Interest>(results, pageable, countInterestsByCompanyId(id));
	}
	
	public long countInterestsByCompanyId(Long id) {
		QInterest interest = QInterest.interest;
		
		return Optional.ofNullable(
				queryFactory
				.select(interest.count())
				.from(interest)
				.join(interest.company).fetchJoin()
				.where(interest.company.id.eq(id))
				.fetchOne())
				.orElse(0L);
	}
}
