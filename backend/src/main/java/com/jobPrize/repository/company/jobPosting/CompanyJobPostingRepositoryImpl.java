package com.jobPrize.repository.company.jobPosting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.company.QJobPosting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class CompanyJobPostingRepositoryImpl implements CompanyJobPostingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<JobPosting> findAllByCompanyId(Long id, Pageable pageable) {
		QJobPosting jobPosting = QJobPosting.jobPosting;

		List<JobPosting> results = queryFactory
				.selectFrom(jobPosting)
				.join(jobPosting.company).fetchJoin()
				.where(jobPosting.company.id.eq(id))
				.orderBy(jobPosting.createdDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		return new PageImpl<JobPosting>(results, pageable, countJobPostingsByCompanyId(id));
	}
	
	@Override
	public  Optional<JobPosting> findWithJobPostingImageByJobPostingId(Long id) {
		QJobPosting jobPosting = QJobPosting.jobPosting;
		
		JobPosting result = queryFactory
				.selectFrom(jobPosting)
				.leftJoin(jobPosting.jobPostingImages).fetchJoin()
				.fetchJoin()
				.where(jobPosting.id.eq(id))
				.distinct()
				.fetchOne();
				return Optional.ofNullable(result);
	}
	
	@Override
    public Optional<JobPosting> findLatestJobPosting(Long id) {
        QJobPosting jobPosting = QJobPosting.jobPosting;
        JobPosting result = queryFactory
        			.selectFrom(jobPosting)
    				.join(jobPosting.company).fetchJoin()
                    .where(jobPosting.company.id.eq(id)) 
                    .orderBy(jobPosting.createdDate.desc())
                    .limit(1)  // 
                    .fetchOne();
		return Optional.ofNullable(result);
        }

	public long countJobPostingsByCompanyId(Long id) {
		QJobPosting jobPosting = QJobPosting.jobPosting;

		return Optional.ofNullable(
				queryFactory
				.select(jobPosting.count())
				.from(jobPosting)
				.where(jobPosting.company.id.eq(id))
				.fetchOne())
				.orElse(0L);
	}
}