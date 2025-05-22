package com.jobPrize.repository.common.subscription;

import java.util.List;
import java.util.Optional;

import com.jobPrize.entity.common.QSubscription;
import com.jobPrize.entity.common.QUser;
import com.jobPrize.entity.common.Subscription;
import com.jobPrize.entity.common.UserType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Subscription> findAll() {	// 모든 구독자 조회
		QSubscription subscription = QSubscription.subscription;
		QUser user = QUser.user;

		List<Subscription> results = queryFactory
				.selectFrom(subscription)
				.leftJoin(subscription.user).fetchJoin()
				.distinct()
				.orderBy(subscription.startDate.desc())
				.fetch();
		return results;
	}
	
	@Override
	public List<Subscription> findAllByUserType(UserType userType){ // UserType에 따른 구독자 조회
		QSubscription subscription = QSubscription.subscription;
		
		List<Subscription> results = queryFactory
				.selectFrom(subscription)
				.leftJoin(subscription.user).fetchJoin()
				.where(subscription.user.type.eq(userType))
				.distinct()
				.orderBy(subscription.startDate.desc())
				.fetch();
		
		return results;
	}
	@Override
	public 	Optional<Subscription> findByCompanyId(Long companyId){
		QSubscription subscription = QSubscription.subscription;

	    return Optional.ofNullable(queryFactory
	        .selectFrom(subscription)
	        .where(subscription.company.id.eq(companyId)
	            .and(subscription.isActive.eq(true))) // ✅ 활성화된 구독만 조회
	        .orderBy(subscription.startDate.desc()) // ✅ 최신순 정렬
	        .fetchFirst()); // ✅ 단건 조회
	}
}
	

