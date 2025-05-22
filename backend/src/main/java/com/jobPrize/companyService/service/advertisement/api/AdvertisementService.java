package com.jobPrize.companyService.service.advertisement.api;

import java.util.List;
import com.jobPrize.companyService.dto.advertisement.AdvertisementResponseDto;

public interface AdvertisementService {
    List<AdvertisementResponseDto> getActiveAdvertisementsByCompany(Long companyId);
}