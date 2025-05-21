package com.jobPrize.repository.common.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.common.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, SubscriptionRepositoryCustom {
	boolean existsByUserId(Long userId);
}