package com.jobPrize.companyService.dto.advertisement;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdvertisementResponseDto {
	private final Long id;
	private final String imageUrl;
	private final LocalDate startDate;
	private final LocalDate endDate;;
	private final boolean isUsingAd;
}
