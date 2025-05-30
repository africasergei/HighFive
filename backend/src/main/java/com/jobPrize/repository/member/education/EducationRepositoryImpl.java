package com.jobPrize.repository.member.education;

import java.util.List;
import java.util.Optional;

import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.QEducation;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EducationRepositoryImpl implements EducationRepositoryCostom{
	
	private final JPAQueryFactory queryFactory;
	@Override
	public List<Education> findAllByMemberId(Long id) {
		QEducation education = QEducation.education;
		
		List<Education> results = queryFactory
				.selectFrom(education)
				.join(education.member).fetchJoin()
				.where(education.member.id.eq(id))
				.orderBy(education.enterDate.desc())
				.fetch();
		
		return results;
	}
	
	@Override
	public Optional<Long> findMemberIdByEducationId(Long id) {
		QEducation education = QEducation.education;

		Long result = queryFactory
			.select(education.member.id)
			.from(education)
			.where(education.id.eq(id))
			.fetchOne();

		return Optional.ofNullable(result);
	}
}
