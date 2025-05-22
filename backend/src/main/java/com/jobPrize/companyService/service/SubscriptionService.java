package com.jobPrize.companyService.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.Subscription.SubscriptionResponseDto;
import com.jobPrize.entity.common.Subscription;
import com.jobPrize.repository.common.subscription.SubscriptionRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SubscriptionService {
private final SubscriptionRepository subscriptionRepository;

@Transactional(readOnly = true)
public SubscriptionResponseDto getMySubscription(Long companyId) {
    Subscription subscription = subscriptionRepository.findByCompanyId(companyId)
        .orElseThrow(() -> new IllegalStateException("기업의 활성화된 구독이 없습니다."));

    return SubscriptionResponseDto.builder()
        .id(subscription.getId())
        .startDate(subscription.getStartDate())
        .endDate(subscription.getEndDate())
        .isSubcribed(subscription.isActive())
        .build();
	}
}



