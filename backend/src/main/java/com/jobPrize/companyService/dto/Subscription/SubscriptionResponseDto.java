package com.jobPrize.companyService.dto.Subscription;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionResponseDto {
	private final Long id;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final boolean isActive;
}
