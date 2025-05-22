package com.jobPrize.repository.memToCom.pass;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.memToCom.Pass;
import com.jobPrize.entity.memToCom.QPass;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PassRepositoryImpl implements PassRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PassRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Pass> findPassedApplicantsByCompanyId(Long id, Pageable pageable) {
        QPass pass = QPass.pass;

        List<Pass> results = queryFactory
            .selectFrom(pass)
            .join(pass.application).fetchJoin()
            .where(pass.application.jobPosting.company.id.eq(id))
            .orderBy(pass.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(results, pageable, countPassedApplicantsByCompanyId(id));
    }
    
    public long countPassedApplicantsByCompanyId(Long id) {
        QPass pass = QPass.pass;

        return Optional.ofNullable(
            queryFactory
                .select(pass.count())
                .from(pass)
                .where(pass.application.jobPosting.company.id.eq(id))
                .fetchOne()
        ).orElse(0L);
    }
}