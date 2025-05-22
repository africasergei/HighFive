package com.jobPrize.companyService.service.advertisement.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.advertisement.AdvertisementResponseDto;
import com.jobPrize.companyService.service.advertisement.api.AdvertisementService;
import com.jobPrize.entity.company.Advertisement;
import com.jobPrize.repository.company.advertisement.AdvertisementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAdvertisementService implements AdvertisementService {
    
    private final AdvertisementRepository advertisementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponseDto> getActiveAdvertisementsByCompany(Long companyId) {
        List<Advertisement> advertisements = advertisementRepository.findAllByCompanyId(companyId);

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