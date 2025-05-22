package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.advertisement.AdvertisementResponseDto;
import com.jobPrize.entity.company.Advertisement;
import com.jobPrize.repository.company.advertisement.AdvertisementRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	private final AdvertisementRepository advertisementRepository;
	
	@Transactional(readOnly = true)
	public List<AdvertisementResponseDto> getActiveAdvertisementsByCompany(Long companyId) {
	    List<Advertisement> advertisements = advertisementRepository.findAllByCompanyId(companyId); // ✅ 모든 광고 조회

	    return advertisements.stream()
	        .filter(Advertisement::isUsingAd) 
	        .map(ad -> AdvertisementResponseDto.builder()
	            .id(ad.getId())
	            .imageUrl(ad.getImageUrl())
	            .startDate(ad.getStartDate())
	            .endDate(ad.getEndDate())
	            .isUsingAd(ad.isUsingAd())
	            .build())
	        .toList();
	}
}
