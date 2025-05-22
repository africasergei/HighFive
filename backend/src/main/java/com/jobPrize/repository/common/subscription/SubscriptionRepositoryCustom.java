package com.jobPrize.repository.common.subscription;

import java.util.List;
import java.util.Optional;

import com.jobPrize.entity.common.Subscription;
import com.jobPrize.entity.common.UserType;

public interface SubscriptionRepositoryCustom {

	List<Subscription> findAll(); // 모든 구독자 조회

	List<Subscription> findAllByUserType(UserType userType); // 사용자 유형에 따른 구독자 정보 조회

	Optional<Subscription> findByCompanyId(Long companyId);
}