package com.jobPrize.repository.memToCom.application;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.company.QJobPosting;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.QApplication;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom{
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public Page<Application> findAllByMemberId(Long id, Pageable pageable) {
		QApplication application = QApplication.application;
		QJobPosting jobPosting = QJobPosting.jobPosting;
		
		List<Application> results = queryFactory
				.selectFrom(application)
				.join(application.member).fetchJoin()
				.join(application.jobPosting,jobPosting).fetchJoin()
				.join(jobPosting.company).fetchJoin()
				.where(application.member.id.eq(id))
				.orderBy(application.createdDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
			    .fetch();
		
		
		return new PageImpl<Application>(results,pageable, countApplicationsByMemberId(id));
	}
	
	@Override
	public Optional<Application> findByApplicationId(Long id) {
		QApplication application = QApplication.application;

		Application result = queryFactory
				.selectFrom(application)
				.join(application).fetchJoin()
				.leftJoin(application.pass).fetchJoin()
				.where(application.id.eq(id))
				.fetchOne();
				
				
		return Optional.ofNullable(result);
	}

	@Override
	public Page<Application> findAllByJobPostingId(Long id, Pageable pageable) {
	QApplication application = QApplication.application;
		
		List<Application> results = queryFactory
				.selectFrom(application)
				.join(application.jobPosting).fetchJoin()
				.where(application.jobPosting.id.eq(id))
				.orderBy(application.createdDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
			    .fetch();
		
		
		return new PageImpl<Application>(results,pageable, countApplicationsByJobPostingId(id));

	}
	
	
	public long countApplicationsByMemberId(Long id) {
		QApplication application = QApplication.application;

	    return Optional.ofNullable(
	    	queryFactory
	        .select(application.count())
	        .from(application)
	        .where(application.member.id.eq(id))
	        .fetchOne())
		    .orElse(0L);
	}
	
	
	public long countApplicationsByJobPostingId(Long id) {
		QApplication application = QApplication.application;

	    return Optional.ofNullable(
	    	queryFactory
	        .select(application.count())
	        .from(application)
	        .where(application.jobPosting.id.eq(id))
	        .fetchOne())
		    .orElse(0L);
	}

}



